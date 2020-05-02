package com.rahul.miner.polarity;

import java.util.List;

import com.rahul.miner.aspect.Aspect;
import com.rahul.miner.opinion_word_extractors.OpinionWord;

public class AspectScoreCalculatorImpl implements AspectScoreCalculator {

	private PolarityGenerator polarityGenerator;

	AspectScoreCalculatorImpl(PolarityGenerator polarityGenerator) {
		this.polarityGenerator = polarityGenerator;

	}

	@Override
	public double calculateAspectScore(Aspect aspect, List<OpinionWord> opinionWords) {
		double sum = 0.0;
		for (OpinionWord word : opinionWords) {
			double wordPolarityScore = this.polarityGenerator.getPolarityScoreForWord(word.getWord());
			if (word.hasNegation()) {
				sum += Math.ceil(10 - wordPolarityScore);
			} else {
				sum += Math.ceil(wordPolarityScore);
			}

		}

		return Math.ceil(sum / opinionWords.size());
	}
}
