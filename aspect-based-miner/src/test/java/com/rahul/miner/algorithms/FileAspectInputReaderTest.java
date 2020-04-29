package com.rahul.miner.algorithms;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.net.URI;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.rahul.miner.aspect.Aspect;
import com.rahul.miner.aspect.AspectInputReader;

class FileAspectInputReaderTest {

	@Test
	void parseAspectfile() throws Exception {

		URI uri = new URI(
				"file:/D:/git/Aspect-Based-Opinion-Miner/aspect-based-miner/src/test/resources/sample_aspects");
		AspectInputReader reader = new FileAspectInputReader();
		List<Aspect> aspects = reader.getAspects(uri);

		assertEquals(3, aspects.size());
	}

}
