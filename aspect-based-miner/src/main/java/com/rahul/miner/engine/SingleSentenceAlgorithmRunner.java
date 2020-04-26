package com.rahul.miner.engine;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.stream.Collectors;

import com.rahul.miner.algorithms.Algorithm;
import com.rahul.miner.algorithms.AlgorithmRunner;
import com.rahul.miner.aspect.Aspect;
import com.rahul.miner.opinion_word_extractors.OpinionWord;

public class SingleSentenceAlgorithmRunner implements AlgorithmRunner {

	private Algorithm algorithm;
	private ExecutorService executorService;

	public SingleSentenceAlgorithmRunner(ExecutorService executorService, Algorithm algorithm) {
		this.executorService = executorService;
		this.algorithm = algorithm;
	}

	@Override
	public CompletableFuture<List<OpinionWord>> run(Aspect aspect, List<String> sentences) {

		CompletableFuture<List<OpinionWord>> result = new CompletableFuture<>();
		List<CompletableFuture<List<OpinionWord>>> pendingExtractions = new ArrayList<>();

		for (String s : sentences) {

			this.algorithm.getExtractors().forEach(extractor -> {
				CompletableFuture<List<OpinionWord>> future = new CompletableFuture<>();
				pendingExtractions.add(future);
				this.executorService.execute(() -> future.complete(extractor.extract(aspect, null)));

			});

		}

		CompletableFuture.allOf(pendingExtractions.toArray(new CompletableFuture[pendingExtractions.size()]))
				.thenAccept((future -> {

					result.complete(pendingExtractions.stream().flatMap(extraction -> extraction.join().stream())
							.collect(Collectors.toList()));

				}));

		return result;
	}

}
