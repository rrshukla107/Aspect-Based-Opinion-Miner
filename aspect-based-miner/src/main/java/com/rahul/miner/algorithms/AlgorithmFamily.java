package com.rahul.miner.algorithms;

import java.util.List;

import com.rahul.miner.opinion_word_extractors.OpinionWordExtractor;

@FunctionalInterface
public interface AlgorithmFamily {

	List<OpinionWordExtractor> getExtractors();

}
