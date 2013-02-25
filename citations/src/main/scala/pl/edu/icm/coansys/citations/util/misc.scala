/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */

package pl.edu.icm.coansys.citations.util

import pl.edu.icm.coansys.importers.models.DocumentProtos.{DocumentWrapper, ReferenceMetadata, BasicMetadata}
import scala.collection.JavaConversions._
import com.nicta.scoobi.core.DList
import pl.edu.icm.coansys.citations.data.CitationEntity
import com.nicta.scoobi.InputsOutputs._
import pl.edu.icm.cermine.bibref.parsing.tools.CitationUtils
import pl.edu.icm.coansys.citations.util.AugmentedDList.augmentDList
import pl.edu.icm.cermine.bibref.CRFBibReferenceParser

/**
 * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
 */
object misc {
  def normalizedAuthorTokensFromAuthorList(meta: BasicMetadata) = {
    meta.getAuthorList.toIterable
      .flatMap {
      author =>
        List(
          author.getName,
          author.getForenames,
          author.getSurname).flatMap(_.split( """[^\p{L}]+"""))
    }
      .filter(_.length > 1)
      .map(_.toLowerCase)
      .toSet
  }

  def readCitationsFromDocumentsFromSeqFiles(uris: List[String], parserModel: String): DList[CitationEntity] = {
    //implicit val documentConverter = new BytesConverter[DocumentMetadata](_.toByteArray, DocumentMetadata.parseFrom(_))
    implicit val referenceConverter = new BytesConverter[ReferenceMetadata](_.toByteArray, ReferenceMetadata.parseFrom(_))
    implicit val wrapperConverter = new BytesConverter[DocumentWrapper](_.toByteArray, DocumentWrapper.parseFrom(_))
    val refmeta = convertValueFromSequenceFile[DocumentWrapper](uris)
      .flatMap(_.getDocumentMetadata.getReferenceList)
    if (parserModel != null)
      refmeta.flatMapWithResource(new CRFBibReferenceParser(parserModel) with NoOpClose) {
        case (parser, meta) if !meta.getRawCitationText.isEmpty =>
          Some(CitationEntity.fromUnparsedReferenceMetadata(parser, meta))
        case _ =>
          None
      }
    else
      refmeta.map(CitationEntity.fromReferenceMetadata(_))
  }

  private val uuidCharset = "UTF-8"

  def uuidEncode(uuid: String): Array[Byte] =
    uuid.getBytes(uuidCharset)

  def uuidDecode(bytes: Array[Byte]): String =
    new String(bytes, uuidCharset)

  def extractNumbers(s: String): List[String] = {
    """(?<=(^|\D))\d+(?=($|\D))""".r.findAllIn(s).toList
  }

  def extractYear(s: String): Option[String] = {
    val baseYear = 2000
    val candidates = extractNumbers(s).filter(_.length == 4).map(_.toInt)

    if (candidates.isEmpty)
      None
    else
      Some(candidates.minBy(x => math.abs(x - baseYear)).toString)

  }

  def tokensFromCermine(s: String): List[String] =
    CitationUtils.stringToCitation(s).getTokens.map(_.getText).toList
}
