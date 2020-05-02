package com.rahul.miner.polarity;

import java.util.List;

import com.rahul.miner.aspect.Aspect;
import com.rahul.miner.opinion_word_extractors.OpinionWord;

public interface AspectScoreCalculator {

	double calculateAspectScore(Aspect aspect, List<OpinionWord> opinonWords);

}
