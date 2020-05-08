package com.rahul.miner.engine;

import java.awt.Color;
import java.awt.Dimension;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.kennycason.kumo.CollisionMode;
import com.kennycason.kumo.PolarBlendMode;
import com.kennycason.kumo.PolarWordCloud;
import com.kennycason.kumo.WordFrequency;
import com.kennycason.kumo.bg.CircleBackground;
import com.kennycason.kumo.font.scale.SqrtFontScalar;
import com.kennycason.kumo.nlp.FrequencyAnalyzer;
import com.kennycason.kumo.palette.ColorPalette;

public class WordCloudGenerator {
	@Test
	public void generateWordCloud() throws IOException {

		final FrequencyAnalyzer frequencyAnalyzer = new FrequencyAnalyzer();
		frequencyAnalyzer.setWordFrequenciesToReturn(10000);
		frequencyAnalyzer.setMinWordLength(0);
		frequencyAnalyzer.setStopWords(loadStopWords());

		final List<WordFrequency> wordFrequencies = frequencyAnalyzer.load(this.getPositiveWords("battery"));
		final List<WordFrequency> wordFrequencies2 = frequencyAnalyzer.load(this.getNegativeWords("battery"));
		final Dimension dimension = new Dimension(600, 600);
		final PolarWordCloud wordCloud = new PolarWordCloud(dimension, CollisionMode.PIXEL_PERFECT,
				PolarBlendMode.BLUR);
		wordCloud.setPadding(3);
		wordCloud.setBackground(new CircleBackground(300));
		wordCloud.setColorPalette(new ColorPalette(new Color(69, 250, 241)));
		wordCloud.setColorPalette2(new ColorPalette(new Color(250, 111, 69)));
		wordCloud.setFontScalar(new SqrtFontScalar(10, 40));
		wordCloud.build(wordFrequencies, wordFrequencies2);
		wordCloud.writeToFile("battery.png");
	}

	private List<String> getPositiveWords(String aspect) throws IOException {
		List<String> words = new ArrayList<>();
		BufferedReader br = new BufferedReader(new FileReader(
				new File("D:\\git\\Aspect-Based-Opinion-Miner\\aspect-based-miner\\src\\test\\resources\\" + aspect)));

		StringBuilder str = new StringBuilder();
		String s = null;
		while ((s = br.readLine()) != null) {
			str.append(s);
		}

		for (String word : str.toString().split(",")) {
			if (word.equals(word.toLowerCase())) {
				words.add(word);
			}
		}
		return words;
	}

	private List<String> getNegativeWords(String aspect) throws IOException {
		List<String> words = new ArrayList<>();
		BufferedReader br = new BufferedReader(new FileReader(
				new File("D:\\git\\Aspect-Based-Opinion-Miner\\aspect-based-miner\\src\\test\\resources\\" + aspect)));

		StringBuilder str = new StringBuilder();
		String s = null;
		while ((s = br.readLine()) != null) {
			str.append(s);
		}

		for (String word : str.toString().split(",")) {
			if (word.equals(word.toUpperCase())) {
				words.add(word);
			}
		}
		return words;
	}

	private Collection<String> loadStopWords() throws IOException {

		List<String> words = new ArrayList<>();
		BufferedReader br = new BufferedReader(new FileReader(new File(
				"D:\\git\\Aspect-Based-Opinion-Miner\\aspect-based-miner\\src\\test\\resources\\stop_words.txt")));

		String s = null;
		while ((s = br.readLine()) != null) {
			words.add(s);
		}
		return words;
	}
}
