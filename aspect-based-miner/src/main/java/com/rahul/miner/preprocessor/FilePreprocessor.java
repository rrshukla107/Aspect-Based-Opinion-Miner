package com.rahul.miner.preprocessor;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import edu.stanford.nlp.ling.HasWord;
import edu.stanford.nlp.ling.Sentence;
import edu.stanford.nlp.process.DocumentPreprocessor;

public class FilePreprocessor {

	public static String getSentences(String inputFile) throws Exception {
		DocumentPreprocessor dp = new DocumentPreprocessor(inputFile);

		String path = "sentences";
		File outputFile = createOutputFile(path);

		try (BufferedWriter bw = new BufferedWriter(new FileWriter(outputFile.getAbsoluteFile()))) {

			for (List<HasWord> sentence : dp) {
				String s = Sentence.listToString(sentence);
				s = s.replaceAll("[^a-zA-Z ]", "");
				s = s.trim(); // trailing and starting spaces
				s = s.replaceAll("\\s+", " "); // merging spaces

				bw.append(s);
				bw.append("\n");
				bw.flush();

			}
		} catch (IOException e) {
			throw new Exception("Error in creating file", e);
		}

		return path;

	}

	private static File createOutputFile(String path) throws IOException {
		File outputFile = createNewFile(Paths.get(path), path);
		return outputFile;
	}

	private static File createNewFile(Path path, String name) throws IOException {
		Files.deleteIfExists(path);

		File outputFile = new File(name);
		outputFile.createNewFile();
		return outputFile;
	}

}
