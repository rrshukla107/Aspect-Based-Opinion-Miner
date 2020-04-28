package com.rahul.miner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.SparkSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rahul.miner.algorithms.AdjectivesExtractionAlgorithmsFamily;
import com.rahul.miner.algorithms.AdverbExtractionAlgorithmsFamily;
import com.rahul.miner.algorithms.VerbExtractionAlgorithmsFamily;
import com.rahul.miner.aspect.Aspect;
import com.rahul.miner.engine.FeatureLevelMiningEngine;
import com.rahul.miner.engine.MiningResult;
import com.rahul.miner.engine.OpinionMiningEngine;
import com.rahul.miner.opinion_word_extractors.OpinionWord;

import edu.stanford.nlp.parser.lexparser.LexicalizedParser;
import scala.Tuple2;

public class AspectBasedOpinionMiner {

	private static final Logger logger = LoggerFactory.getLogger(AspectBasedOpinionMiner.class);

	private static String grammar = "edu/stanford/nlp/models/lexparser/englishPCFG.ser.gz";
	private static String[] options = { "-maxLength", "80", "-retainTmpSubcategories" };

	private static OpinionMiningEngine engine = new FeatureLevelMiningEngine(
			List.of(new AdjectivesExtractionAlgorithmsFamily(), new AdverbExtractionAlgorithmsFamily(),
					new VerbExtractionAlgorithmsFamily()),
			4, LexicalizedParser.loadModel(grammar, options));

	public static void main(String[] args) throws IOException {

		// Add preprocessor

		// Take input of the aspects

		Aspect direction = new Aspect(List.of("direction", "director"));
		Aspect script = new Aspect(List.of("script", "plot", "screenplay", "story"));
		Aspect acting = new Aspect(List.of("actor", "actors", "acting"));

		List<Aspect> aspects = List.of(direction, script, acting);

		SparkSession spark = SparkSession.builder().appName("AspectBasedOpinionMining").getOrCreate();
//		JavaRDD<String> lines = spark.read().textFile(args[0]).javaRDD();

		JavaRDD<String> lines = spark.read().textFile(
				"D:\\git\\Aspect-Based-Opinion-Miner\\aspect-based-miner\\src\\test\\resources\\Movie_Reviews_Output.txt"
						+ "")
				.javaRDD();

		List<JavaPairRDD<Aspect, String>> aspectSentences = new ArrayList<>();

		for (Aspect aspect : aspects) {

			aspectSentences.add(lines.filter(line -> {

				boolean result = false;
				for (String feature : aspect.getAspects()) {
					result = result || line.contains(feature);
				}
				return result;

			}).mapToPair(line -> new Tuple2<>(aspect, line)));
		}

//		JavaSparkContext.fromSparkContext(spark.sparkContext());

		JavaPairRDD<Aspect, String> union = JavaSparkContext.fromSparkContext(spark.sparkContext())
				.union(aspectSentences.toArray(new JavaPairRDD[aspectSentences.size()]));

		JavaPairRDD<Aspect, List<String>> reduceByKey = union.mapToPair((tuple -> {
			List<String> list = new ArrayList<String>();
			list.add(tuple._2());
			return new Tuple2<Aspect, List<String>>(tuple._1(), list);
		})).reduceByKey((sentence1, sentence2) -> {
			sentence1.addAll(sentence2);
			return sentence1;
		});

		JavaRDD<Tuple2<Aspect, List<String>>> map = reduceByKey.map(aspectDetails -> {
			logger.info("###################yosssssss");
			MiningResult miningResult = engine.process(aspectDetails._1(), aspectDetails._2()).get();
			List<String> opinionWord = miningResult.getOpinionWord().stream().map(OpinionWord::toString)
					.collect(Collectors.toList());
			return new Tuple2<Aspect, List<String>>(miningResult.getAspect(), opinionWord);
		});

		map.saveAsTextFile("D:\\git\\Aspect-Based-Opinion-Miner\\aspect-based-miner\\TEST");
		logger.info("###################yo");

	}
}
