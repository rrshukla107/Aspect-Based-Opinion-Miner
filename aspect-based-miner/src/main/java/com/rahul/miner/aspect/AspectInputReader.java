package com.rahul.miner.aspect;

import java.net.URI;
import java.util.List;

public interface AspectInputReader {

	List<Aspect> getAspects(URI path) throws Exception;
}
