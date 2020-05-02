package com.rahul.miner.algorithms;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import com.rahul.miner.opinion_word_extractors.OpinionWordExtractor;

import edu.stanford.nlp.trees.TypedDependency;

public class AdjectivesExtractionAlgorithmsFamily implements AlgorithmFamily {

	@Override
	public List<OpinionWordExtractor> getExtractors() {
		return List.of(new Algorithm1(), new Algorithm2(), new Algorithm3(), new Algorithm5(), new Algorithm6());
	}

	private class Algorithm1 implements OpinionWordExtractor {

		@Override
		public Optional<String> extract(String feature, Collection<TypedDependency> td) {
			Optional<String> opinionWord = Optional.empty();
			String x = null, y = null, y1 = null;

			for (TypedDependency t : td) {
				if (t.reln().getShortName().equals("cc") && t.dep().originalText().equals("but")) {
					y = t.gov().originalText();
					for (TypedDependency t1 : td) {
						if (t1.reln().getShortName().equals("nsubj") && t1.gov().originalText().equals(y)) {
							x = t1.dep().originalText();
							if (x.equals("it") || x.equals(feature)) {
								System.out.println(feature + " ****1 - " + y);
								opinionWord = Optional.of(y);
								break;
							}
						}
					}
				}
			}

			return opinionWord;
		}

		@Override
		public String getName() {
			return "algo 1";
		}

	}

	private class Algorithm2 implements OpinionWordExtractor {

		@Override
		public Optional<String> extract(String feature, Collection<TypedDependency> td) {
			Optional<String> opinionWord = Optional.empty();

			String y = null;

			for (TypedDependency t : td) {
				if (t.reln().getShortName().equals("amod") || t.reln().getShortName().equals("rcmod")
						|| t.reln().getShortName().equals("advmod")) {
					if (t.gov().originalText().equals(feature)) {
						y = t.dep().originalText().toString();
						System.out.println(feature + " ****2.1 - " + y);
						opinionWord = Optional.of(y);
						break;

					}
				}
			}

			return opinionWord;
		}

		@Override
		public String getName() {
			return "algo 2";
		}

	}

	private class Algorithm3 implements OpinionWordExtractor {

		@Override
		public Optional<String> extract(String feature, Collection<TypedDependency> td) {
			Optional<String> opinionWord = Optional.empty();

			for (TypedDependency t : td) {
				if (t.reln().getShortName().equals("prep_like") && t.dep().originalText().equals(feature)) {
					System.out.println(feature + " ****3 - " + "like");
					opinionWord = Optional.of("like");

				}
			}

			return opinionWord;
		}

		@Override
		public String getName() {
			return "algo 3";
		}

	}

	private class Algorithm5 implements OpinionWordExtractor {

		@Override
		public String getName() {
			return "algo 5";
		}

		@Override
		public Optional<String> extract(String feature, Collection<TypedDependency> td) {
			Optional<String> opinionWord = Optional.empty();

			String x = null, y = null;

			for (TypedDependency t : td) {
				if (t.reln().getShortName().equals("root") && t.gov().originalText().equals("")) {
					// "" --> empty for root element, i.e, ROOT
					y = t.dep().originalText();
					for (TypedDependency t1 : td) {
						if (t1.reln().getShortName().equals("nsubj") && t1.gov().originalText().equals(y)
								&& t1.dep().originalText().equals(feature)) {
							for (TypedDependency t2 : td) {
								if (t2.reln().getShortName().equals("cop") && t2.gov().originalText().equals(y)) {
									System.out.println(feature + " ****5 - " + y);
									opinionWord = Optional.of(y);
								}
								break;
							}
						}
					}
				}
			}

			return opinionWord;
		}
	}

	private class Algorithm6 implements OpinionWordExtractor {

		@Override
		public String getName() {
			return "algo 6";
		}

		@Override
		public Optional<String> extract(String feature, Collection<TypedDependency> td) {
			Optional<String> opinionWord = Optional.empty();
			String x = null, y = null;

			for (TypedDependency t : td) {
				if (t.reln().getShortName().equals("nsubj") && t.dep().originalText().equals(feature)) {

					x = t.gov().originalText();
					for (TypedDependency t1 : td) {
						if (t1.reln().getShortName().equals("acomp") && t1.gov().originalText().equals(x)) {
							y = t.dep().originalText();
							System.out.println(feature + " ****6 - " + y);
							opinionWord = Optional.of(y);
							break;
						}
					}
				}
			}

			return opinionWord;

		}

	}

	@Override
	public String getName() {
		return this.getClass().getName();
	}

}
