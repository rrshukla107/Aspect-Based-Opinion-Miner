package com.rahul.miner.polarity;

public interface PolarityGenerator {

	Double getPolarityScoreForWord(String word);
	
	Boolean isEmotive(String word);

}
