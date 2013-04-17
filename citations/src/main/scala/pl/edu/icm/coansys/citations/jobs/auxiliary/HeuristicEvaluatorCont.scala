/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */

package pl.edu.icm.coansys.citations.jobs.auxiliary

import com.nicta.scoobi.application.ScoobiApp
import com.nicta.scoobi.InputsOutputs
import pl.edu.icm.coansys.commons.scala.xml
import pl.edu.icm.coansys.citations.util.AugmentedDList.augmentDList
import pl.edu.icm.cermine.bibref.CRFBibReferenceParser
import pl.edu.icm.coansys.citations.util.{matching, NoOpClose}
import pl.edu.icm.coansys.citations.data.MatchableEntity
import pl.edu.icm.coansys.citations.indices.AuthorIndex
import com.nicta.scoobi.Persist._
import com.nicta.scoobi.InputsOutputs._

/**
 * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
 */
object HeuristicEvaluatorCont extends ScoobiApp {
  override def upload = false

  def run() {
    val parserModel = args(0)
    val authorIndex = args(1)
    val citationUri = args(2)
    val outUri = args(3)

    val result =
      InputsOutputs.convertFromSequenceFile[String, String](citationUri).map {
        case (id, xmlString) =>
          val trimmed = xml.removeTags(xmlString, " ").trim.dropWhile(!_.isLetter).trim
          (id, trimmed)
      }.mapWithResource(new CRFBibReferenceParser(parserModel) with NoOpClose) {
        case (parser, (id, ref)) =>
          (id, MatchableEntity.fromUnparsedReference(parser, id, ref))
      }.mapWithResource(new AuthorIndex(authorIndex)) {
        case (index, (id, entity)) =>
          (id, matching.approximatelyMatchingDocuments(entity, index))
      }.map {
        case (id, matching) =>
          (id, matching.mkString(" "))
      }
    persist(convertToSequenceFile(result, outUri, overwrite = true))
  }
}
