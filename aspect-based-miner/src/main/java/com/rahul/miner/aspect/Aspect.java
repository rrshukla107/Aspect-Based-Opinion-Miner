package com.rahul.miner.aspect;

import java.io.Serializable;
import java.util.List;

public class Aspect implements Serializable {

	private static final long serialVersionUID = -1753103002722107494L;

	private List<String> aspects;

	public Aspect(List<String> aspects) {
		this.aspects = aspects;
	}

	public List<String> getAspects() {
		return aspects;
	}

}
