/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */

package pl.edu.icm.coansys.citations.data.feature_calculators

import pl.edu.icm.coansys.citations.data.MatchableEntity
import pl.edu.icm.coansys.commons.scala.strings
import pl.edu.icm.coansys.citations.util.misc._
import pl.edu.icm.coansys.citations.util.classification.features.FeatureCalculator

/**
 * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
 */
object SourceRawTextMatchFactor  extends FeatureCalculator[(MatchableEntity, MatchableEntity)] {
  def calculateValue(entities: (MatchableEntity, MatchableEntity)): Double = {
    safeDiv(strings.lcs(entities._1.source, entities._2.rawText.getOrElse("")).length, entities._1.source.length) max
      safeDiv(strings.lcs(entities._2.source, entities._1.rawText.getOrElse("")).length, entities._2.source.length)
  }

}
