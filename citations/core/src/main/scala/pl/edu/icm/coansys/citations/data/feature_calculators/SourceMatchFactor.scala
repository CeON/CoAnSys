/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */

package pl.edu.icm.coansys.citations.data.feature_calculators

import pl.edu.icm.coansys.citations.data.MatchableEntity
import pl.edu.icm.coansys.commons.scala.strings
import pl.edu.icm.coansys.citations.util.classification.features.FeatureCalculator

/**
 * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
 */
object SourceMatchFactor extends FeatureCalculator[(MatchableEntity, MatchableEntity)] {
  def calculateValue(entities: (MatchableEntity, MatchableEntity)): Double = {
    val minLen = math.min(entities._1.source.length, entities._2.source.length)
    if (minLen > 0) {
      val lcs = strings.lcs(entities._1.source, entities._2.source)
      lcs.length.toDouble / minLen
    }
    else
      0.0
  }

}
