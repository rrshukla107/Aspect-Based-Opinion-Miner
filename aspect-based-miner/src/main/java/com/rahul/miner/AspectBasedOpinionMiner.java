package com.rahul.miner;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.sql.SparkSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rahul.miner.algorithms.AdjectivesExtractionAlgorithmsFamily;
import com.rahul.miner.algorithms.FileAspectInputReader;
import com.rahul.miner.algorithms.VerbExtractionAlgorithmsFamily;
import com.rahul.miner.aspect.Aspect;
import com.rahul.miner.aspect.AspectInputReader;
import com.rahul.miner.engine.FeatureLevelMiningEngine;
import com.rahul.miner.engine.MiningResult;
import com.rahul.miner.engine.OpinionMiningEngine;
import com.rahul.miner.opinion_word_extractors.OpinionWord;
import com.rahul.miner.polarity.AspectScoreCalculator;
import com.rahul.miner.polarity.AspectScoreCalculatorImpl;
import com.rahul.miner.polarity.PolarityGenerator;
import com.rahul.miner.polarity.PolarityGeneratorImpl;
import com.rahul.miner.preprocessor.FilePreprocessor;

import edu.stanford.nlp.parser.lexparser.LexicalizedParser;
import scala.Tuple2;

public class AspectBasedOpinionMiner {

	private static final String POSITIVE_WORDS = "miner.positive_words_lexicon";
	private static final String NEGATIVE_WORDS = "miner.negative_words_lexicon";
	private static final String WORD_NET_PATH = "miner.sentiWordNet_path";
	private static final String OPINION_WORD_OUTPUT_DIRECTORY = "miner.output.aspect.opinion_words";
	private static final String OPINION_WORD_SCORE_DIRECTORY = "miner.output.aspect.scores";

	private static final String CONFIGURATION = "miner_configuration.properties";
	private static final Properties PROPERTIES = initializeProperties();

	private static final String ASPECT_BASED_OPINION_MINING = "AspectBasedOpinionMining";

	private static final Logger logger = LoggerFactory.getLogger(AspectBasedOpinionMiner.class);

	private static String GRAMMAR = "edu/stanford/nlp/models/lexparser/englishPCFG.ser.gz";
	private static String[] OPTIONS = { "-maxLength", "80", "-retainTmpSubcategories" };

	private static PolarityGenerator polarityGenerator = new PolarityGeneratorImpl(
			new File(PROPERTIES.getProperty(POSITIVE_WORDS)), new File(PROPERTIES.getProperty(NEGATIVE_WORDS)),
			PROPERTIES.getProperty(WORD_NET_PATH));

	private static OpinionMiningEngine ENGINE = new FeatureLevelMiningEngine(
			List.of(new AdjectivesExtractionAlgorithmsFamily(), new VerbExtractionAlgorithmsFamily()), 4,
			LexicalizedParser.loadModel(GRAMMAR, OPTIONS), polarityGenerator);

	private static AspectScoreCalculator SCORE_CALCULATOR = new AspectScoreCalculatorImpl(polarityGenerator);

	private static AspectInputReader ASPECT_READER = new FileAspectInputReader();

	public static void main(String[] args) throws Exception {

		SparkSession spark = SparkSession.builder().appName(ASPECT_BASED_OPINION_MINING).getOrCreate();

		String reviewFilePath = args[0];
		String aspectFilePath = args[1];

		String preprocessedFile = FilePreprocessor.writeSentences(reviewFilePath);

		List<Aspect> aspects = ASPECT_READER.getAspects(new File(aspectFilePath));

		logger.info(" *** ASPECT BASED OPINION MINING STARTED *** ");
		JavaRDD<String> lines = spark.read().textFile(preprocessedFile).javaRDD();
		JavaPairRDD<Aspect, String> aspectSentencesMapping = mapAspectToSentence(spark, aspects, lines);
		JavaPairRDD<Aspect, List<String>> sentencesForAspect = reduceToFindSentencesForEachAspect(
				aspectSentencesMapping);
		JavaRDD<Tuple2<Aspect, List<OpinionWord>>> opinonWordsForAspect = extractOpinionWordsForAspects(
				sentencesForAspect);
		opinonWordsForAspect.saveAsTextFile(OPINION_WORD_OUTPUT_DIRECTORY);
		JavaPairRDD<Aspect, Double> aspectScores = calculateAspectScore(opinonWordsForAspect);
		aspectScores.saveAsTextFile(OPINION_WORD_SCORE_DIRECTORY);
		logger.info(" *** ASPECT BASED OPINION MINING COMPLETE *** ");

	}

	private static JavaPairRDD<Aspect, Double> calculateAspectScore(JavaRDD<Tuple2<Aspect, List<OpinionWord>>> result) {
		JavaPairRDD<Aspect, Double> mapToPair = result.mapToPair(aspectResults -> {

			return new Tuple2<Aspect, Double>(aspectResults._1(),
					SCORE_CALCULATOR.calculateAspectScore(aspectResults._1(), aspectResults._2()));
		});
		return mapToPair;
	}

	private static JavaRDD<Tuple2<Aspect, List<OpinionWord>>> extractOpinionWordsForAspects(
			JavaPairRDD<Aspect, List<String>> reduceByKey) {
		return reduceByKey.map(aspectDetails -> {
			logger.info("OPINION MINING STARTED FOR ASPECT - " + aspectDetails._1());
			MiningResult miningResult = ENGINE.process(aspectDetails._1(), aspectDetails._2()).get();
			return new Tuple2<Aspect, List<OpinionWord>>(miningResult.getAspect(), miningResult.getOpinionWord());
		});
	}

	private static JavaPairRDD<Aspect, List<String>> reduceToFindSentencesForEachAspect(
			JavaPairRDD<Aspect, String> union) {
		JavaPairRDD<Aspect, List<String>> reduceByAspect = union.mapToPair((tuple -> {
			List<String> list = new ArrayList<String>();
			list.add(tuple._2());
			return new Tuple2<Aspect, List<String>>(tuple._1(), list);
		})).reduceByKey((sentence1, sentence2) -> {
			sentence1.addAll(sentence2);
			return sentence1;
		});
		return reduceByAspect;
	}

	private static JavaPairRDD<Aspect, String> mapAspectToSentence(SparkSession spark, List<Aspect> aspects,
			JavaRDD<String> lines) {
		List<JavaPairRDD<Aspect, String>> aspectSentences = new ArrayList<>();
		aspects.forEach(aspect -> aspectSentences
				.add(lines.filter(containsAspect(aspect)).mapToPair(line -> new Tuple2<>(aspect, line))));

		@SuppressWarnings("unchecked")
		JavaPairRDD<Aspect, String> union = JavaSparkContext.fromSparkContext(spark.sparkContext())
				.union(aspectSentences.toArray(new JavaPairRDD[aspectSentences.size()]));
		return union;
	}

	private static Function<String, Boolean> containsAspect(Aspect aspect) {
		return line -> {

			boolean result = false;
			for (String feature : aspect.getAspects()) {
				result = result || line.contains(feature);
			}
			return result;

		};
	}

	private static Properties initializeProperties() {
		String resourceName = CONFIGURATION;
		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		Properties props = new Properties();
		try (InputStream resourceStream = loader.getResourceAsStream(resourceName)) {
			props.load(resourceStream);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return props;
	}
}
