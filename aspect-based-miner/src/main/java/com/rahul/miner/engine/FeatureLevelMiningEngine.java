package com.rahul.miner.engine;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import com.rahul.miner.algorithms.AlgorithmFamily;
import com.rahul.miner.algorithms.AlgorithmRunner;
import com.rahul.miner.aspect.Aspect;
import com.rahul.miner.opinion_word_extractors.OpinionWord;

import edu.stanford.nlp.parser.lexparser.LexicalizedParser;

public class FeatureLevelMiningEngine implements OpinionMiningEngine {

	private ExecutorService executorService;
	private List<AlgorithmFamily> algorithms;
	private LexicalizedParser parser;

	public FeatureLevelMiningEngine(List<AlgorithmFamily> algorithms, int numThreads, LexicalizedParser parser) {

		this.algorithms = algorithms;
		this.parser = parser;
		this.executorService = Executors.newFixedThreadPool(numThreads);

	}

	@Override
	public CompletableFuture<MiningResult> process(Aspect feature, List<String> sentences) {

		CompletableFuture<MiningResult> result = new CompletableFuture<>();
		List<CompletableFuture<List<OpinionWord>>> runningAlgorithms = this.algorithms.stream().map(algo -> {
			AlgorithmRunner runner = new SingleSentenceAlgorithmRunner(executorService, algo, parser);
			return runner.run(feature, sentences);
		}).collect(Collectors.toList());

		CompletableFuture.allOf(runningAlgorithms.toArray(new CompletableFuture[runningAlgorithms.size()]))
				.thenAccept(future -> {
					result.complete(this.createResult(
							runningAlgorithms.stream().flatMap(r -> r.join().stream()).collect(Collectors.toList()),
							feature));

				});

		return result;

	}

	private MiningResult createResult(List<OpinionWord> opinionWords, Aspect feature) {
		return new MiningResult(feature, opinionWords);
	}

}
