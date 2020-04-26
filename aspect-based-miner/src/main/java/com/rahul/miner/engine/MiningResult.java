package com.rahul.miner.engine;

import java.util.List;

import com.rahul.miner.aspect.Aspect;
import com.rahul.miner.opinion_word_extractors.OpinionWord;

public class MiningResult {

	private Aspect aspect;
	private List<OpinionWord> opinionWord;

	public MiningResult(Aspect aspect, List<OpinionWord> opinionWord) {
		this.aspect = aspect;
		this.opinionWord = opinionWord;
	}

	public Aspect getAspect() {
		return aspect;
	}

	public List<OpinionWord> getOpinionWord() {
		return opinionWord;
	}

}
