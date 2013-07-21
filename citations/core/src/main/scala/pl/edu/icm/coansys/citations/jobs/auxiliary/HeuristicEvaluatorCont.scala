/*
 * (C) 2010-2012 ICM UW. All rights reserved.
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
