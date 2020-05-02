package com.rahul.miner.polarity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class PolarityGeneratorImpl implements PolarityGenerator {

	private static final String ADJECTIVE = "a";
	private static final String NOUN = "n";

	private Set<String> positive;
	private Set<String> negative;
	private SentiWordNetDemoCode wordNet;

	public PolarityGeneratorImpl(File positiveWords, File negativeWords, String pathToSentiWordNet) {

		this.positive = new HashSet<String>();
		this.negative = new HashSet<String>();

		try (BufferedReader positiveReader = new BufferedReader(new FileReader(positiveWords.getAbsolutePath()));
				BufferedReader negativeReader = new BufferedReader(new FileReader(negativeWords.getAbsolutePath()));) {
			this.wordNet = new SentiWordNetDemoCode(pathToSentiWordNet);
			String line;
			while ((line = positiveReader.readLine()) != null) {
				positive.add(line);
			}
			while ((line = negativeReader.readLine()) != null) {
				negative.add(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private double getWordNetScore(String word) {
		double score;
		if ((score = wordNet.extract(word, ADJECTIVE)) == 0) {
			score = wordNet.extract(word, NOUN);
		}
		return score;
	}

	@Override
	public Double getPolarityScoreForWord(String word) {
		// EXPERIMENT HERE...!!
		double score = 0;
		if (positive.contains(word)) {
			score += 6;
		} else if (negative.contains(word)) {
			score += 3;
		} else {
			// the word is neutral or not an opinion word itself or ex, "emotive"
			score = 5;
			double wordNetScore = this.getWordNetScore(word);
			if (wordNetScore > 0) {
				score += 2;
			} else if (wordNetScore < 0) {
				score -= 2;
			}
		}

		// score += 3 * Math.pow(getWordNetScore(word), 2);
		score += 3 * getWordNetScore(word);
		return score;
	}

}
