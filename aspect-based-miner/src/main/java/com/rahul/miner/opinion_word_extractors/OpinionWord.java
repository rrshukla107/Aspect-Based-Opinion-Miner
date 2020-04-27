package com.rahul.miner.opinion_word_extractors;

public class OpinionWord {

	private String word;

	private Boolean isNegation;

	public OpinionWord(String word) {
		this.word = word;
	}

	public OpinionWord(String word, Boolean isNegation) {
		this.word = word;
		this.isNegation = isNegation;
	}

	public String getWord() {
		return word;
	}

	public void setWord(String word) {
		this.word = word;
	}

	public Boolean getIsNegation() {
		return isNegation;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((word == null) ? 0 : word.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		OpinionWord other = (OpinionWord) obj;
		if (word == null) {
			if (other.word != null)
				return false;
		} else if (!word.equals(other.word))
			return false;
		return true;
	}

}
