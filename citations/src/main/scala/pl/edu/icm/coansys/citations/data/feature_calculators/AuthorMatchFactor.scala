/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */

package pl.edu.icm.coansys.citations.data.feature_calculators

import pl.edu.icm.cermine.tools.classification.features.FeatureCalculator
import pl.edu.icm.coansys.citations.data.Entity
import pl.edu.icm.coansys.citations.util.author_matching
import pl.edu.icm.coansys.citations.util.misc._

/**
 * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
 */
object AuthorMatchFactor extends FeatureCalculator[Entity, Entity] {
  def calculateFeatureValue(e1: Entity, e2: Entity) =
    author_matching.matchFactor(tokensFromCermine(e1.author), tokensFromCermine(e2.author))
}
