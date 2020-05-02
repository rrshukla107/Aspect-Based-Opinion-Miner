//package com.rahul.miner.algorithms;
//
//import java.util.ArrayList;
//import java.util.Collection;
//import java.util.List;
//
//import com.rahul.miner.aspect.Aspect;
//import com.rahul.miner.opinion_word_extractors.OpinionWord;
//import com.rahul.miner.opinion_word_extractors.OpinionWordExtractor;
//
//import edu.stanford.nlp.trees.GrammaticalStructure;
//import edu.stanford.nlp.trees.TypedDependency;
//
//public class AdverbExtractionAlgorithmsFamily implements AlgorithmFamily {
//
//	@Override
//	public List<OpinionWordExtractor> getExtractors() {
//		return List.of(new Algorithm1(), new Algorithm2(), new Algorithm3());
//	}
//
//	private class Algorithm1 implements OpinionWordExtractor {
//
//		@Override
//		public List<OpinionWord> extract(Aspect aspect, GrammaticalStructure gs) {
//			List<OpinionWord> opinionWords = new ArrayList<>();
//			Collection<TypedDependency> td = gs.allTypedDependencies();
//			String y = null;
//
//			for (String feature : aspect.getAspects()) {
//
//				for (TypedDependency t : td) {
//					if (t.reln().getShortName().equals("root") && t.gov().originalText().equals("")) {
//						y = t.dep().originalText();
//
//						for (TypedDependency t1 : td) {
//							if (t1.reln().getShortName().equals("nsubj") && t1.gov().originalText().equals(y)
//									&& t1.dep().originalText().equals(feature)) {
//								System.out.println(aspect + " #####1 - " + y);
//								opinionWords.add(NegationUtils.setNegationForWord(y, td));
//
//							}
//						}
//					}
//				}
//			}
//
//			return opinionWords;
//		}
//
//		@Override
//		public String getName() {
//			return "algo 1";
//		}
//
//	}
//
//	private class Algorithm2 implements OpinionWordExtractor {
//
//		@Override
//		public List<OpinionWord> extract(Aspect aspect, GrammaticalStructure gs) {
//			List<OpinionWord> opinionWords = new ArrayList<>();
//			Collection<TypedDependency> td = gs.allTypedDependencies();
//			String y = null;
//
//			for (String feature : aspect.getAspects()) {
//				for (TypedDependency t : td) {
//					if (t.reln().getShortName().equals("root") && t.gov().originalText().equals("")) {
//						y = t.dep().originalText();
//						for (TypedDependency t1 : td) {
//							if ((t1.reln().getShortName().equals("dobj") && t1.gov().originalText().equals(y)
//									&& t1.dep().originalText().equals(feature))
//									|| (t1.reln().getShortName().equals("pobj") && t1.gov().originalText().equals(y)
//											&& t1.dep().originalText().equals(feature))) {
//								System.out.println(aspect + " #####2 - " + y);
//								opinionWords.add(NegationUtils.setNegationForWord(y, td));
//							}
//						}
//					}
//				}
//			}
//
//			return opinionWords;
//		}
//
//		@Override
//		public String getName() {
//			return "algo 2";
//		}
//
//	}
//
//	public class Algorithm3 implements OpinionWordExtractor {
//
//		@Override
//		public List<OpinionWord> extract(Aspect aspect, GrammaticalStructure gs) {
//			List<OpinionWord> opinionWords = new ArrayList<>();
//			Collection<TypedDependency> td = gs.allTypedDependencies();
//			String y = null;
//
//			for (String feature : aspect.getAspects()) {
//				for (TypedDependency t : td) {
//					if (t.reln().getShortName().equals("root") && t.gov().originalText().equals("")) {
//						y = t.dep().originalText();
//						for (TypedDependency t1 : td) {
//							if ((t1.reln().getShortName().equals("nsubj_pass") && t1.gov().originalText().equals(y)
//									&& t1.dep().originalText().equals(feature))
//									|| (t1.reln().getShortName().equals("xsubj") && t1.gov().originalText().equals(y)
//											&& t1.dep().originalText().equals(feature))) {
//								System.out.println(aspect + " #####3 - " + y);
//								opinionWords.add(NegationUtils.setNegationForWord(y, td));
//							}
//						}
//					}
//				}
//			}
//
//			return opinionWords;
//		}
//
//		@Override
//		public String getName() {
//			return "algo 3";
//		}
//
//	}
//
//}
