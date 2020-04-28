package com.rahul.miner.engine;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.rahul.miner.algorithms.AdjectivesExtractionAlgorithmsFamily;
import com.rahul.miner.algorithms.AdverbExtractionAlgorithmsFamily;
import com.rahul.miner.algorithms.AlgorithmFamily;
import com.rahul.miner.algorithms.VerbExtractionAlgorithmsFamily;
import com.rahul.miner.aspect.Aspect;
import com.rahul.miner.opinion_word_extractors.OpinionWord;

import core.Preprocess;
import edu.stanford.nlp.parser.lexparser.LexicalizedParser;

public class FeatureLevelMiningEngineTest {

	private OpinionMiningEngine engine;

	private static String grammar = "edu/stanford/nlp/models/lexparser/englishPCFG.ser.gz";
	private static String[] options = { "-maxLength", "80", "-retainTmpSubcategories" };

	@Test
	public void whenExtractionComplete_FutureComplete() {

		CountDownLatch latch = new CountDownLatch(1);

		AlgorithmFamily dummyAlgo1 = () -> Arrays.asList((aspect, gsf) -> {
			try {
				TimeUnit.SECONDS.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return Arrays.asList(new OpinionWord("test1"), new OpinionWord("test2"));
		}, (aspect, gsf) -> Arrays.asList(new OpinionWord("test3"), new OpinionWord("test4")));

		AlgorithmFamily dummyAlgo2 = () -> Arrays.asList((aspect, gsf) -> Arrays.asList(new OpinionWord("test5")),
				(aspect, gsf) -> Arrays.asList(new OpinionWord("test6")));

		this.engine = new FeatureLevelMiningEngine(List.of(dummyAlgo1, dummyAlgo2), 4,
				LexicalizedParser.loadModel(grammar, options));

		CompletableFuture<MiningResult> process = this.engine.process(null, Arrays.asList("sentence 1", "sentence 2"));

		process.thenAccept(miningResult -> {
			assertEquals(12, miningResult.getOpinionWord().size());
			assertTrue(miningResult.getOpinionWord().contains(new OpinionWord("test3")));
			latch.countDown();
		});

		try {
			latch.await(100000, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			fail();
		}

	}

	@Test
	public void test() throws IOException {

		Preprocess preprocessor = new Preprocess(
				"D:\\git\\Aspect-Based-Opinion-Miner\\aspect-based-miner\\src\\test\\resources\\Movie_Reviews.txt",
				"D:\\git\\Aspect-Based-Opinion-Miner\\aspect-based-miner\\src\\test\\resources\\Movie_Reviews_Output.txt");

		File file = new File(
				"D:\\git\\Aspect-Based-Opinion-Miner\\aspect-based-miner\\src\\test\\resources\\Movie_Reviews_Output.txt");

		List<String> lines = new ArrayList<String>();
		try (BufferedReader br = new BufferedReader(new FileReader(file));) {
			String line;
			while ((line = br.readLine()) != null) {
				lines.add(line);
			}

		}

		CountDownLatch latch = new CountDownLatch(1);

		this.engine = new FeatureLevelMiningEngine(List.of(new AdjectivesExtractionAlgorithmsFamily(),
				new AdverbExtractionAlgorithmsFamily(), new VerbExtractionAlgorithmsFamily()), 4,
				LexicalizedParser.loadModel(grammar, options));

		CompletableFuture<MiningResult> process = this.engine
				.process(new Aspect(Arrays.asList("acting", "direction", "cast", "story")), lines);

		process.thenAccept(miningResult -> {
			assertEquals(12, miningResult.getOpinionWord().size());
			assertTrue(miningResult.getOpinionWord().contains(new OpinionWord("test3")));
			latch.countDown();
		});

		try {
			latch.await();
		} catch (InterruptedException e) {
			fail();
		}

	}

}
