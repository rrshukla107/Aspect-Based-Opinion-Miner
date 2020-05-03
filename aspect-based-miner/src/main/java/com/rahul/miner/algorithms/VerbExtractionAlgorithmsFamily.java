package com.rahul.miner.algorithms;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import com.rahul.miner.opinion_word_extractors.OpinionWordExtractor;

import edu.stanford.nlp.trees.TypedDependency;

public class VerbExtractionAlgorithmsFamily implements AlgorithmFamily {

	@Override
	public List<OpinionWordExtractor> getExtractors() {
		return List.of(new Algorithm1(), new Algorithm2(), new Algorithm3());
	}

	private class Algorithm1 implements OpinionWordExtractor {

		@Override
		public Optional<String> extract(String feature, Collection<TypedDependency> td) {

			Optional<String> opinionWord = Optional.empty();
			String y = null;

			for (TypedDependency t : td) {
				if (t.reln().getShortName().equals("root") && t.gov().originalText().equals("")) {
					y = t.dep().originalText();

					for (TypedDependency t1 : td) {
						if (t1.reln().getShortName().equals("nsubj") && t1.gov().originalText().equals(y)
								&& t1.dep().originalText().equals(feature)) {
//							System.out.println(feature + " #####1 - " + y);
							opinionWord = Optional.of(y);
							break;
						}
					}
				}
			}

			return opinionWord;
		}

		@Override
		public String getName() {
			return "Verb algo 1";
		}

	}

	private class Algorithm2 implements OpinionWordExtractor {

		@Override
		public Optional<String> extract(String feature, Collection<TypedDependency> td) {

			Optional<String> opinionWord = Optional.empty();
			String y = null;

			for (TypedDependency t : td) {
				if (t.reln().getShortName().equals("root") && t.gov().originalText().equals("")) {
					y = t.dep().originalText();
					for (TypedDependency t1 : td) {
						if ((t1.reln().getShortName().equals("dobj") && t1.gov().originalText().equals(y)
								&& t1.dep().originalText().equals(feature))
								|| (t1.reln().getShortName().equals("pobj") && t1.gov().originalText().equals(y)
										&& t1.dep().originalText().equals(feature))) {
//							System.out.println(feature + " #####2 - " + y);
							opinionWord = Optional.of(y);
							break;

						}
					}
				}
			}

			return opinionWord;
		}

		@Override
		public String getName() {
			return "Verb algo 2";
		}

	}

	private class Algorithm3 implements OpinionWordExtractor {

		@Override
		public Optional<String> extract(String feature, Collection<TypedDependency> td) {

			Optional<String> opinionWord = Optional.empty();
			String y = null;

			for (TypedDependency t : td) {
				if (t.reln().getShortName().equals("root") && t.gov().originalText().equals("")) {
					y = t.dep().originalText();
					for (TypedDependency t1 : td) {
						if ((t1.reln().getShortName().equals("nsubj_pass") && t1.gov().originalText().equals(y)
								&& t1.dep().originalText().equals(feature))
								|| (t1.reln().getShortName().equals("xsubj") && t1.gov().originalText().equals(y)
										&& t1.dep().originalText().equals(feature))) {
							opinionWord = Optional.of(y);
							break;
						}
					}
				}
			}
			return opinionWord;

		}

		@Override
		public String getName() {
			return "Verb algo 3";
		}

	}

	@Override
	public String getName() {
		return this.getClass().getName();
	}

}
