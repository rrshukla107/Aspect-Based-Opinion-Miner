package com.rahul.miner.aspect;

import java.io.File;
import java.util.List;

public interface AspectInputReader {

	List<Aspect> getAspects(File file) throws Exception;
}
