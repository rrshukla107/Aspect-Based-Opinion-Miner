package com.rahul.miner.algorithms;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.rahul.miner.opinion_word_extractors.OpinionWord;
import com.rahul.miner.polarity.PolarityGenerator;

import edu.stanford.nlp.trees.TypedDependency;

public class AdverbUtils {

	public static List<OpinionWord> getAdverbs(String aspect, String opinionWord, Collection<TypedDependency> td,
			PolarityGenerator polarityGenerator) {

		String y = null;

		List<OpinionWord> words = new ArrayList<>();

		for (TypedDependency t : td) {
			if (t.reln().getShortName().equals("advmod") && t.gov().originalText().equals(opinionWord)) {
				y = t.dep().originalText();
				System.out.println(aspect + " %%%%%1 - " + y);
				words.add(NegationUtils.setNegationForWord(y, td, polarityGenerator));
			}

			if (t.reln().getShortName().equals("advcl") && t.gov().originalText().equals(opinionWord)) {
				y = t.dep().originalText();
				System.out.println(aspect + " %%%%%2 - " + y);
				words.add(NegationUtils.setNegationForWord(y, td, polarityGenerator));
			}

			if (t.reln().getShortName().equals("amod") && t.gov().originalText().equals(opinionWord)) {
				y = t.dep().originalText();
				System.out.println(aspect + " %%%%%3 - " + y);
				words.add(NegationUtils.setNegationForWord(y, td, polarityGenerator));
			}
		}

		return words;

	}

}
