package com.rahul.miner.engine;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.rahul.miner.algorithms.AlgorithmFamily;
import com.rahul.miner.aspect.Aspect;
import com.rahul.miner.opinion_word_extractors.OpinionWord;

import edu.stanford.nlp.parser.lexparser.LexicalizedParser;
import edu.stanford.nlp.trees.GrammaticalStructure;
import edu.stanford.nlp.trees.GrammaticalStructureFactory;
import edu.stanford.nlp.trees.TreebankLanguagePack;

public class FeatureLevelMiningEngine implements OpinionMiningEngine {

	private ExecutorService executorService;
	private List<AlgorithmFamily> algorithms;
	private LexicalizedParser parser;
	private GrammaticalStructureFactory gsf;
	private TreebankLanguagePack tlp;

	public FeatureLevelMiningEngine(List<AlgorithmFamily> algorithms, int numThreads, LexicalizedParser parser) {

		this.algorithms = algorithms;
		this.parser = parser;
		this.tlp = parser.getOp().langpack();
		this.gsf = tlp.grammaticalStructureFactory();
		this.executorService = Executors.newFixedThreadPool(numThreads);

	}

	@Override
	public CompletableFuture<MiningResult> process(Aspect aspect, List<String> sentences) {

		CompletableFuture<MiningResult> result = new CompletableFuture<>();

		CompletableFuture<List<OpinionWord>>[] promises = IntStream.rangeClosed(0, sentences.size() - 1)
				.mapToObj(i -> new CompletableFuture<List<OpinionWord>>()).collect(Collectors.toList())
				.toArray(new CompletableFuture[sentences.size()]);

		IntStream.rangeClosed(0, promises.length - 1).forEach(i -> {

			this.executorService.submit(() -> {
				try {
					GrammaticalStructure structure = this.gsf
							.newGrammaticalStructure(this.parser.parse(sentences.get(i)));
					List<OpinionWord> collect = this.algorithms.stream()
							.flatMap(algoFamily -> algoFamily.getExtractors().stream())
							.map(extractor -> extractor.extract(aspect, structure)).flatMap(words -> words.stream())
							.collect(Collectors.toList());

					promises[i].complete(collect);

				} catch (Exception e) {
					e.printStackTrace();
					promises[i].complete(Collections.emptyList());
				}
			});
		});

		CompletableFuture.allOf(promises).thenAccept(promise -> {

			List<OpinionWord> words = new ArrayList<>();

			for (CompletableFuture<List<OpinionWord>> p : promises) {
				words.addAll(p.join());
			}

			result.complete(this.createResult(words, aspect));

		});

		return result;

	}

	private MiningResult createResult(List<OpinionWord> opinionWords, Aspect feature) {
		return new MiningResult(feature, opinionWords);
	}

}
