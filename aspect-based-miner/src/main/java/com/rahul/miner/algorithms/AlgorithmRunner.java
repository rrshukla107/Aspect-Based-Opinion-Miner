package com.rahul.miner.algorithms;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import com.rahul.miner.aspect.Aspect;
import com.rahul.miner.opinion_word_extractors.OpinionWord;

public interface AlgorithmRunner {

	CompletableFuture<List<OpinionWord>> run(Aspect aspect, List<String> sentences);

}
