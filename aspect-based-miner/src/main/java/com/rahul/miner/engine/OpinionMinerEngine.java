package com.rahul.miner.engine;

import java.util.List;

import com.rahul.miner.aspect.Aspect;

public interface OpinionMinerEngine {

	public MiningResult mineOpinions(List<Aspect> features, List<String> sentences);

}
