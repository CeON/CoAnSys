/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */

package pl.edu.icm.coansys.citations.data.feature_calculators

import pl.edu.icm.coansys.citations.data.MatchableEntity
import pl.edu.icm.coansys.citations.util.misc
import pl.edu.icm.coansys.citations.util.classification.features.FeatureCalculator

/**
 * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
 */
object PagesMatchFactor  extends FeatureCalculator[(MatchableEntity, MatchableEntity)] {
  def calculateValue(entities: (MatchableEntity, MatchableEntity)): Double = {
    val pages1 = misc.extractNumbers(entities._1.pages)
    val pages2 = misc.extractNumbers(entities._2.pages)
    if (pages1.size + pages2.size > 0)
      2 * (pages1.toSet & pages2.toSet).size.toDouble / (pages1.size + pages2.size)
    else
      0.0
  }
}
