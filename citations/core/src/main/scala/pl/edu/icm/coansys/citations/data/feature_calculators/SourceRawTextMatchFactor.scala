/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */

package pl.edu.icm.coansys.citations.data.feature_calculators

import pl.edu.icm.cermine.tools.classification.features.FeatureCalculator
import pl.edu.icm.coansys.citations.data.MatchableEntity
import pl.edu.icm.coansys.commons.scala.strings
import pl.edu.icm.coansys.citations.util.misc._

/**
 * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
 */
object SourceRawTextMatchFactor extends FeatureCalculator[MatchableEntity, MatchableEntity] {
  def calculateFeatureValue(e1: MatchableEntity, e2: MatchableEntity) = {
    safeDiv(strings.lcs(e1.source, e2.rawText.getOrElse("")).length, e1.source.length) max
      safeDiv(strings.lcs(e2.source, e1.rawText.getOrElse("")).length, e2.source.length)
  }

}
