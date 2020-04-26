package com.rahul.miner.engine;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.rahul.miner.algorithms.AdjectivesAlgorithms;
import com.rahul.miner.algorithms.Algorithm;
import com.rahul.miner.aspect.Aspect;
import com.rahul.miner.opinion_word_extractors.OpinionWord;

import edu.stanford.nlp.parser.lexparser.LexicalizedParser;

public class FeatureLevelMiningEngineTest {

	private OpinionMiningEngine engine;

	private static String grammar = "edu/stanford/nlp/models/lexparser/englishPCFG.ser.gz";
	private static String[] options = { "-maxLength", "80", "-retainTmpSubcategories" };

	@Test
	public void whenExtractionComplete_FutureComplete() {

		CountDownLatch latch = new CountDownLatch(1);

		Algorithm dummyAlgo1 = () -> Arrays.asList((aspect, gsf) -> {
			try {
				TimeUnit.SECONDS.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return Arrays.asList(new OpinionWord("test1"), new OpinionWord("test2"));
		}, (aspect, gsf) -> Arrays.asList(new OpinionWord("test3"), new OpinionWord("test4")));

		Algorithm dummyAlgo2 = () -> Arrays.asList((aspect, gsf) -> Arrays.asList(new OpinionWord("test5")),
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
	public void test() {

		CountDownLatch latch = new CountDownLatch(1);

		this.engine = new FeatureLevelMiningEngine(List.of(new AdjectivesAlgorithms()), 4,
				LexicalizedParser.loadModel(grammar, options));

		CompletableFuture<MiningResult> process = this.engine.process(new Aspect(Arrays.asList("abc", "cde")),
				Arrays.asList("sentence 1", "sentence 2"));

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

}
