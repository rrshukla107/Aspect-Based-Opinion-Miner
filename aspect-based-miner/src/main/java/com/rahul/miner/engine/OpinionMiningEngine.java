package com.rahul.miner.engine;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import com.rahul.miner.aspect.Aspect;
import com.rahul.miner.opinion_word_extractors.OpinionWordExtractor;

public interface OpinionMiningEngine {

	CompletableFuture<MiningResult> process(Aspect feature, List<String> sentences);

	List<OpinionWordExtractor> getExtractors();

}
