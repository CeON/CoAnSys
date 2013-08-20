/*
 * This file is part of CoAnSys project.
 * Copyright (c) 20012-2013 ICM-UW
 * 
 * CoAnSys is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.

 * CoAnSys is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with CoAnSys. If not, see <http://www.gnu.org/licenses/>.
 */

package pl.edu.icm.coansys.citations.jobs.auxiliary

import com.nicta.scoobi.Scoobi._
import pl.edu.icm.coansys.citations.util.AugmentedDList.augmentDList
import pl.edu.icm.coansys.citations.util.{nlm, XPathEvaluator, matching}
import pl.edu.icm.coansys.citations.data.MatchableEntity
import pl.edu.icm.coansys.citations.indices.AuthorIndex
import org.apache.commons.io.IOUtils

/**
 * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
 */
object HeuristicEvaluatorCont extends ScoobiApp {
  def run() {
    val authorIndex = args(0)
    val citationUri = args(1)
    val outUri = args(2)

    val result =
      fromSequenceFile[String, String](citationUri).map {
        case (id, xmlString) =>
          val eval = XPathEvaluator.fromInputStream(IOUtils.toInputStream(xmlString))
          val ref = nlm.referenceMetadataBuilderFromNode(eval.asNode("."))
          (id, (xmlString, MatchableEntity.fromReferenceMetadata(ref.build())))
      }.mapWithResource(new AuthorIndex(authorIndex)) {
        case (index, (id, (xmlString, entity))) =>
          (id, (xmlString, matching.approximatelyMatchingDocuments(entity, index)))
      }.map {
        case (id, (xmlString, matching)) =>
          (id, matching.mkString(" ") + "\n" + xmlString)
      }
    persist(toSequenceFile(result, outUri, overwrite = true))
  }
}
