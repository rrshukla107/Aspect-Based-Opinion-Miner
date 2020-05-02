package com.rahul.miner.aspect;

import java.io.Serializable;
import java.util.List;

public class Aspect implements Serializable {

	private static final long serialVersionUID = -4750380542979649057L;

	private List<String> aspects;
	private String aspectName;

	public Aspect(String aspectName, List<String> aspects) {
		this.aspectName = aspectName;
		this.aspects = aspects;
	}

	public List<String> getAspects() {
		return aspects;
	}

	public String getAspectName() {
		return aspectName;
	}

	@Override
	public String toString() {
		return this.aspectName + " | " + aspects;
	}

}
