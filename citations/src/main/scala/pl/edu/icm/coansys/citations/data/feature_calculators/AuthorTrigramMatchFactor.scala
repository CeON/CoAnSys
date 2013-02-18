/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */

package pl.edu.icm.coansys.citations.data.feature_calculators

import pl.edu.icm.cermine.tools.classification.features.FeatureCalculator
import pl.edu.icm.coansys.citations.data.Entity
import pl.edu.icm.coansys.citations.util.ngrams._

/**
 * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
 */
object AuthorTrigramMatchFactor extends FeatureCalculator[Entity, Entity] {
  def calculateFeatureValue(e1: Entity, e2: Entity) =
    trigramSimilarity(e1.author, e2.author)
}
