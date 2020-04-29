package com.rahul.miner.algorithms;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import com.rahul.miner.aspect.Aspect;
import com.rahul.miner.aspect.AspectInputReader;

public class FileAspectInputReader implements AspectInputReader {

	// ASPECT_1|ASPECT_1_SYNONYM_1|ASPECT_1_SYNONYM_2|...|ASPECT_1_SYNONYM_m
	// ASPECT_2|ASPECT_2_SYNONYM_1|ASPECT_2_SYNONYM_2|...|ASPECT_2_SYNONYM_m
	// .
	// .
	// .
	// ASPECT_n|ASPECT_n_SYNONYM_1|ASPECT_n_SYNONYM_2|...|ASPECT_n_SYNONYM_m

	@Override
	public List<Aspect> getAspects(URI path) throws Exception {

		File file = new File(path);

		String s;

		List<Aspect> aspects = new ArrayList<>();

		try (BufferedReader br = new BufferedReader(new FileReader(file))) {
			while ((s = br.readLine()) != null) {
				List<String> aspect = List.of(s.toLowerCase().split("\\|"));
				aspects.add(new Aspect(aspect.get(0), aspect));
			}

		} catch (Exception e) {
			throw new Exception("Problem in parsing aspect file input", e);
		}

		return aspects;
	}

}
