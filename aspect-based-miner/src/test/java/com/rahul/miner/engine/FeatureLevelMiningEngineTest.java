package com.rahul.miner.engine;

public class FeatureLevelMiningEngineTest {

	private OpinionMiningEngine engine;

	private static String grammar = "edu/stanford/nlp/models/lexparser/englishPCFG.ser.gz";
	private static String[] options = { "-maxLength", "80", "-retainTmpSubcategories" };

//	@Test
//	public void whenExtractionComplete_FutureComplete() {
//
//		CountDownLatch latch = new CountDownLatch(1);
//
//		AlgorithmFamily dummyAlgo1 = () -> Arrays.asList((aspect, gsf) -> {
//			try {
//				TimeUnit.SECONDS.sleep(1);
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
//			return Arrays.asList(new OpinionWord("test1"), new OpinionWord("test2"));
//		}, (aspect, gsf) -> Arrays.asList(new OpinionWord("test3"), new OpinionWord("test4")));
//
//		AlgorithmFamily dummyAlgo2 = () -> Arrays.asList((aspect, gsf) -> Arrays.asList(new OpinionWord("test5")),
//				(aspect, gsf) -> Arrays.asList(new OpinionWord("test6")));
//
//		this.engine = new FeatureLevelMiningEngine(List.of(dummyAlgo1, dummyAlgo2), 4,
//				LexicalizedParser.loadModel(grammar, options));
//
//		CompletableFuture<MiningResult> process = this.engine.process(null, Arrays.asList("sentence 1", "sentence 2"));
//
//		process.thenAccept(miningResult -> {
//			assertEquals(12, miningResult.getOpinionWord().size());
//			assertTrue(miningResult.getOpinionWord().contains(new OpinionWord("test3")));
//			latch.countDown();
//		});
//
//		try {
//			latch.await(100000, TimeUnit.SECONDS);
//		} catch (InterruptedException e) {
//			fail();
//		}
//
//	}
//
//	@Test
//	public void test() throws IOException {
//
//		Preprocess preprocessor = new Preprocess(
//				"D:\\git\\Aspect-Based-Opinion-Miner\\aspect-based-miner\\src\\test\\resources\\Movie_Reviews.txt",
//				"D:\\git\\Aspect-Based-Opinion-Miner\\aspect-based-miner\\src\\test\\resources\\Movie_Reviews_Output.txt");
//
//		File file = new File(
//				"D:\\git\\Aspect-Based-Opinion-Miner\\aspect-based-miner\\src\\test\\resources\\Movie_Reviews_Output.txt");
//
//		List<String> lines = new ArrayList<String>();
//		try (BufferedReader br = new BufferedReader(new FileReader(file));) {
//			String line;
//			while ((line = br.readLine()) != null) {
//				lines.add(line);
//			}
//
//		}
//
//		CountDownLatch latch = new CountDownLatch(1);
//
//		this.engine = new FeatureLevelMiningEngine(List.of(new AdjectivesExtractionAlgorithmsFamily(),
//				new AdverbExtractionAlgorithmsFamily(), new VerbExtractionAlgorithmsFamily()), 4,
//				LexicalizedParser.loadModel(grammar, options));
//
//		CompletableFuture<MiningResult> process = this.engine
//				.process(new Aspect("acting", Arrays.asList("acting", "direction", "cast", "story")), lines);
//
//		process.thenAccept(miningResult -> {
//			assertEquals(12, miningResult.getOpinionWord().size());
//			assertTrue(miningResult.getOpinionWord().contains(new OpinionWord("test3")));
//			latch.countDown();
//		});
//
//		try {
//			latch.await();
//		} catch (InterruptedException e) {
//			fail();
//		}
//
//	}

}
