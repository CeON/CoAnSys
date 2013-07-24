/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */

package pl.edu.icm.coansys.citations.data.feature_calculators

import pl.edu.icm.coansys.citations.data.MatchableEntity
import pl.edu.icm.coansys.citations.util.misc
import pl.edu.icm.coansys.citations.util.misc.safeDiv
import pl.edu.icm.coansys.citations.util.classification.features.FeatureCalculator

/**
 * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
 */
object PagesRawTextMatchFactor  extends FeatureCalculator[(MatchableEntity, MatchableEntity)] {
  def calculateValue(entities: (MatchableEntity, MatchableEntity)): Double = {
    val pages1 = misc.extractNumbers(entities._1.pages)
    val pages2 = misc.extractNumbers(entities._2.pages)
    val pagesRaw1 = misc.extractNumbers(entities._1.rawText.getOrElse(""))
    val pagesRaw2 = misc.extractNumbers(entities._2.rawText.getOrElse(""))

    safeDiv((pagesRaw2.toSet & pages1.toSet).size, pages1.size) max
      safeDiv((pagesRaw1.toSet & pages2.toSet).size, pages2.size)
  }
}
