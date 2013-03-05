/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */

package pl.edu.icm.coansys.citations.data.feature_calculators

import pl.edu.icm.cermine.tools.classification.features.FeatureCalculator
import pl.edu.icm.coansys.citations.data.Entity
import pl.edu.icm.coansys.commons.scala.strings

/**
 * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
 */
object SourceMatchFactor extends FeatureCalculator[Entity, Entity] {
  def calculateFeatureValue(e1: Entity, e2: Entity) = {
    val minLen = math.min(e1.source.length, e2.source.length)
    if (minLen > 0) {
      val lcs = strings.lcs(e1.source, e2.source)
      lcs.length.toDouble / minLen
    }
    else
      0.0
  }

}
