package com.rahul.miner.opinion_word_extractors;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import com.rahul.miner.algorithms.AdverbUtils;
import com.rahul.miner.algorithms.NegationUtils;
import com.rahul.miner.aspect.Aspect;

import edu.stanford.nlp.trees.GrammaticalStructure;
import edu.stanford.nlp.trees.TypedDependency;

@FunctionalInterface
public interface OpinionWordExtractor {

	Optional<String> extract(String feature, Collection<TypedDependency> td);

	default String getName() {
		return null;
	};

	default public List<OpinionWord> getOpinionWords(Aspect aspect, GrammaticalStructure gs) {
		List<OpinionWord> opinionWords = new ArrayList<>();

		for (String feature : aspect.getAspects()) {
			Collection<TypedDependency> td = gs.allTypedDependencies();

			this.extract(feature, td).ifPresent(opinion -> {
				opinionWords.add(NegationUtils.setNegationForWord(opinion, td));
				opinionWords.addAll(AdverbUtils.getAdverbs(feature, opinion, td));
			});

		}

		return opinionWords;
	}
}
