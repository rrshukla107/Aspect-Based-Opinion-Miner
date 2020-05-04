package com.rahul.miner.algorithms;

import java.util.Collection;

import com.rahul.miner.opinion_word_extractors.OpinionWord;
import com.rahul.miner.polarity.PolarityGenerator;

import edu.stanford.nlp.trees.TypedDependency;

public class NegationUtils {

	public static OpinionWord setNegationForWord(String word, Collection<TypedDependency> td,
			PolarityGenerator polarityGenerator) {
		boolean negation = false;

		for (TypedDependency t : td) {
			if (t.reln().getShortName().equals("neg") && t.gov().originalText().equals(word))
				negation = true;
			if (t.reln().getShortName().equals("conj_negcc") && t.dep().originalText().equals(word))
				negation = true;
			if (t.reln().getShortName().equals("pobj") && t.dep().originalText().equals(word)
					&& t.gov().originalText().equals("not"))
				negation = true;
		}

		if (!negation) {
			negation = polarityGenerator.isNegative(word);
		}

		return new OpinionWord(word, negation);

	}
}
