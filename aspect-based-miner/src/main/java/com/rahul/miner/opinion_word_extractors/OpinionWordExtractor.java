package com.rahul.miner.opinion_word_extractors;

import java.util.List;

import com.rahul.miner.aspect.Aspect;

import edu.stanford.nlp.trees.GrammaticalStructure;

@FunctionalInterface
public interface OpinionWordExtractor {

	List<OpinionWord> extract(Aspect aspect, GrammaticalStructure gsf);

}
