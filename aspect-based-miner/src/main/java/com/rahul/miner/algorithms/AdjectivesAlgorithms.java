package com.rahul.miner.algorithms;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.rahul.miner.aspect.Aspect;
import com.rahul.miner.opinion_word_extractors.OpinionWord;
import com.rahul.miner.opinion_word_extractors.OpinionWordExtractor;

import edu.stanford.nlp.trees.GrammaticalStructure;
import edu.stanford.nlp.trees.TypedDependency;

public class AdjectivesAlgorithms implements Algorithm {

	@Override
	public List<OpinionWordExtractor> getExtractors() {
		return List.of(this::algorithm1);
	}

	List<OpinionWord> algorithm1(Aspect aspect, GrammaticalStructure gs) {

		List<OpinionWord> opinionWords = new ArrayList<>();
		Collection<TypedDependency> td = gs.allTypedDependencies();

		for (String feature : aspect.getAspects()) {
			String x = null, y = null, y1 = null;
			for (TypedDependency t : td) {
				if (t.reln().getShortName().equals("cc") && t.dep().originalText().equals("but")) {
					y = t.gov().originalText();
					for (TypedDependency t1 : td) {
						if (t1.reln().getShortName().equals("nsubj") && t1.gov().originalText().equals(y)) {
							x = t1.dep().originalText();
							if (x.equals("it") || x.equals(aspect)) {
								System.out.println(aspect + " ****1 - " + y);
								opinionWords.add(new OpinionWord(y));
//								if (checkNegation(y, td)) {
//									System.out.println("negation present in " + y);
//									opinionWords.add(y)
//									insertOpinionWord(aspect, y, true);
//								} else {
//									insertOpinionWord(aspect, y, false);
//								}

								break;
							}
						}
					}
				}
			}

		}
		return opinionWords;
	}

	private boolean checkNegation(String word, Collection<TypedDependency> td) {
		String y;
		boolean flag = false;

		for (TypedDependency t : td) {
			if (t.reln().getShortName().equals("neg") && t.gov().originalText().equals(word))
				flag = true;
			if (t.reln().getShortName().equals("conj_negcc") && t.dep().originalText().equals(word))
				flag = true;
			if (t.reln().getShortName().equals("pobj") && t.dep().originalText().equals(word)
					&& t.gov().originalText().equals("not"))
				flag = true;
		}

		return flag;
	}

}
