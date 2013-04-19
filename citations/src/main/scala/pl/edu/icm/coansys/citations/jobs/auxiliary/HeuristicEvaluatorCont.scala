/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */

package pl.edu.icm.coansys.citations.jobs.auxiliary

import com.nicta.scoobi.application.ScoobiApp
import com.nicta.scoobi.InputsOutputs
import pl.edu.icm.coansys.commons.scala.xml
import pl.edu.icm.coansys.citations.util.AugmentedDList.augmentDList
import pl.edu.icm.cermine.bibref.CRFBibReferenceParser
import pl.edu.icm.coansys.citations.util.{nlm, XPathEvaluator, matching, NoOpClose}
import pl.edu.icm.coansys.citations.data.MatchableEntity
import pl.edu.icm.coansys.citations.indices.AuthorIndex
import com.nicta.scoobi.Persist._
import com.nicta.scoobi.InputsOutputs._
import org.apache.commons.io.IOUtils

/**
 * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
 */
object HeuristicEvaluatorCont extends ScoobiApp {
  override def upload = false

  def run() {
    val authorIndex = args(0)
    val citationUri = args(1)
    val outUri = args(2)

    val result =
      InputsOutputs.convertFromSequenceFile[String, String](citationUri).map {
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
    persist(convertToSequenceFile(result, outUri, overwrite = true))
  }
}
