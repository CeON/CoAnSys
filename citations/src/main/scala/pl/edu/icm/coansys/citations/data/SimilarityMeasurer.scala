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
class SimilarityMeasurer {
  val featureVectorBuilder = new FeatureVectorBuilder[Entity, Entity]
  featureVectorBuilder.setFeatureCalculators(List(
    AuthorTrigramMatchFactor,
    AuthorTokenMatchFactor,
    PagesMatchFactor,
    SourceMatchFactor,
    TitleMatchFactor,
    YearMatchFactor))

  val classifier = new SVMClassifier[Entity, Entity, MatchingResult](featureVectorBuilder, classOf[MatchingResult]) {}
  classifier.loadModelFromResources("/pl/edu/icm/coansys/citations/pubmedMatching.model", null)

  def similarity(e1: Entity, e2: Entity): Double =
    classifier.predictProbabilities(e1, e2)(MatchingResult.Match)
}

object SimilarityMeasurer {
  def main(args: Array[String]) {
    val measurer = new SimilarityMeasurer
    val doc1 = new DocumentEntityMock("Jan Kowalski", "J. App. Phis.", "Some random title", "120-126", "2010")
    val doc2 = new DocumentEntityMock("Jan Kowalski", "J. App. Phis.", "Totally different title", "32-36", "2010")
    val doc3 = new DocumentEntityMock("Zbigniew Nowak", "Przegląd leśniczy", "Inny tytuł", "15-20", "1995")
    println(measurer.similarity(doc1, doc1))
    println(measurer.similarity(doc1, doc2))
    println(measurer.similarity(doc1, doc3))

  }
}
