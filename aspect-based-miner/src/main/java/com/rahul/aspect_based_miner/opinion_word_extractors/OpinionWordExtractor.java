package com.rahul.aspect_based_miner.opinion_word_extractors;

import java.util.List;

import edu.stanford.nlp.trees.GrammaticalStructureFactory;

@FunctionalInterface
public interface OpinionWordExtractor {

	List<OpinionWord> extract(Aspect aspect, GrammaticalStructureFactory gsf);

}
