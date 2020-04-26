package com.rahul.miner.engine;

import java.util.List;

import com.rahul.miner.aspect.Aspect;

public interface OpinionMiningEngine {

	public MiningResult process(List<Aspect> features, List<String> sentences);

}
