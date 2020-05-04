package com.rahul.miner.opinion_word_extractors;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rahul.miner.algorithms.AdverbUtils;
import com.rahul.miner.algorithms.NegationUtils;
import com.rahul.miner.aspect.Aspect;
import com.rahul.miner.polarity.PolarityGenerator;

import edu.stanford.nlp.trees.GrammaticalStructure;
import edu.stanford.nlp.trees.TypedDependency;

@FunctionalInterface
public interface OpinionWordExtractor {

	static final Logger LOGGER = LoggerFactory.getLogger(OpinionWordExtractor.class);

	Optional<String> extract(String feature, Collection<TypedDependency> td);

	default String getName() {
		return null;
	};

	default public List<OpinionWord> getOpinionWords(Aspect aspect, String line, GrammaticalStructure gs,
			PolarityGenerator polarityGenerator) {
		List<OpinionWord> opinionWords = new ArrayList<>();

		for (String feature : aspect.getAspects()) {
			Collection<TypedDependency> td = gs.allTypedDependencies();

			this.extract(feature, td).ifPresent(opinion -> {

				if (polarityGenerator.isEmotive(opinion)) {

					LOGGER.info("**Feature - " + feature + " **ALGO - " + this.getName() + " **OpinionWord - " + opinion
							+ " **Sentence - " + line);

					opinionWords.add(NegationUtils.setNegationForWord(opinion, td, polarityGenerator));

					AdverbUtils.getAdverbs(feature, opinion, td, polarityGenerator).forEach(verb -> {
						if (polarityGenerator.isEmotive(verb.getWord())) {
							opinionWords.add(verb);
						}
					});

				} else {
					LOGGER.info("*OpinionWord with no emotion - " + opinion);
				}

			});

		}

		return opinionWords;
	}
}
