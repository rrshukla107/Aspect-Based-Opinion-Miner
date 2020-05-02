package com.rahul.miner.engine;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.stream.Collectors;

import com.rahul.miner.algorithms.AlgorithmFamily;
import com.rahul.miner.algorithms.AlgorithmRunner;
import com.rahul.miner.aspect.Aspect;
import com.rahul.miner.opinion_word_extractors.OpinionWord;

import edu.stanford.nlp.parser.lexparser.LexicalizedParser;
import edu.stanford.nlp.trees.GrammaticalStructure;
import edu.stanford.nlp.trees.GrammaticalStructureFactory;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreebankLanguagePack;

public class SingleSentenceAlgorithmRunner implements AlgorithmRunner {

	private AlgorithmFamily algorithm;
	private ExecutorService executorService;
	private LexicalizedParser parser;
	private TreebankLanguagePack tlp;
	private GrammaticalStructureFactory gsf;

	public SingleSentenceAlgorithmRunner(ExecutorService executorService, AlgorithmFamily algorithm,
			LexicalizedParser parser) {
		this.executorService = executorService;
		this.algorithm = algorithm;
		this.parser = parser;
		this.tlp = parser.getOp().langpack();
		this.gsf = tlp.grammaticalStructureFactory();
	}

	@Override
	public CompletableFuture<List<OpinionWord>> run(Aspect aspect, List<String> sentences) {

		CompletableFuture<List<OpinionWord>> result = new CompletableFuture<>();
		List<CompletableFuture<List<OpinionWord>>> pendingExtractions = new ArrayList<>();

		for (String s : sentences) {

			CompletableFuture.supplyAsync(() -> {

				return gsf.newGrammaticalStructure(parser.parse(s));
			}).thenAcceptAsync((structure) -> {
				this.algorithm.getExtractors().forEach(extractor -> {
					CompletableFuture<List<OpinionWord>> future = new CompletableFuture<>();
					pendingExtractions.add(future);
//					this.executorService.execute(() -> future.complete(extractor.extract(aspect, structure)));

				});
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
