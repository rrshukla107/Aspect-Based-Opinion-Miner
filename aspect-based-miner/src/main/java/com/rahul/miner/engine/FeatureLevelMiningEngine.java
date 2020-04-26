package com.rahul.miner.engine;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import com.rahul.miner.algorithms.Algorithm;
import com.rahul.miner.aspect.Aspect;
import com.rahul.miner.opinion_word_extractors.OpinionWord;
import com.rahul.miner.opinion_word_extractors.OpinionWordExtractor;

public class FeatureLevelMiningEngine implements OpinionMiningEngine {

	private List<OpinionWordExtractor> extractors;

	private ExecutorService executorService;

	public FeatureLevelMiningEngine(List<Algorithm> algorithms, int numThreads) {

		this.extractors = algorithms.stream().flatMap(algo -> algo.getAlgorithms().stream())
				.collect(Collectors.toList());

		this.executorService = Executors.newFixedThreadPool(numThreads);
	}

	@Override
	public List<OpinionWordExtractor> getExtractors() {
		return this.extractors;
	}

	@Override
	public CompletableFuture<MiningResult> process(Aspect feature, List<String> sentences) {

		CompletableFuture<MiningResult> result = new CompletableFuture<MiningResult>();
		List<CompletableFuture<List<OpinionWord>>> pendingExtractions = new ArrayList<>();

		this.extractors.forEach(extractor -> {
			CompletableFuture<List<OpinionWord>> future = new CompletableFuture<>();
			pendingExtractions.add(future);
			this.executorService.execute(() -> future.complete(extractor.extract(feature, null)));

		});

		CompletableFuture.allOf(pendingExtractions.toArray(new CompletableFuture[pendingExtractions.size()]))
				.thenAccept((future -> {

					result.complete(this.createResult(
							pendingExtractions.stream().flatMap(r -> r.join().stream()).collect(Collectors.toList()),
							feature));

				}));

		return result;

	}

	private MiningResult createResult(List<OpinionWord> pendingExtractions, Aspect feature) {
		return new MiningResult(feature, pendingExtractions);
	}

}
