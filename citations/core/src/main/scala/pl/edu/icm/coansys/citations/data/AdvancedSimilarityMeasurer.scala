/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */

package pl.edu.icm.coansys.citations.data

import collection.JavaConversions._
import feature_calculators._
import pl.edu.icm.cermine.tools.classification.svm.SVMClassifier
import pl.edu.icm.cermine.tools.classification.features.FeatureVectorBuilder

/**
 * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
 */
class AdvancedSimilarityMeasurer {
  val featureVectorBuilder = new FeatureVectorBuilder[MatchableEntity, MatchableEntity]
  featureVectorBuilder.setFeatureCalculators(List(
    AuthorMatchFactor,
    AuthorTrigramMatchFactor,
    AuthorTokenMatchFactor,
    PagesMatchFactor,
    PagesRawTextMatchFactor,
    SourceMatchFactor,
    SourceRawTextMatchFactor,
    TitleMatchFactor,
    TitleTokenMatchFactor,
    YearMatchFactor,
    YearRawTextMatchFactor))

  val classifier = new SVMClassifier[MatchableEntity, MatchableEntity, MatchingResult](featureVectorBuilder, classOf[MatchingResult]) {}
  classifier.loadModelFromResources("/pl/edu/icm/coansys/citations/pubmedMatchingBetter.model", null)

  def similarity(e1: MatchableEntity, e2: MatchableEntity): Double =
    classifier.predictProbabilities(e1, e2)(MatchingResult.Match)
}


