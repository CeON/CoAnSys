/*
 * This file is part of CoAnSys project.
 * Copyright (c) 2012-2013 ICM-UW
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

package pl.edu.icm.coansys.citations.util

import pl.edu.icm.coansys.models.DocumentProtos.{DocumentWrapper, ReferenceMetadata, BasicMetadata}
import scala.collection.JavaConversions._
import com.nicta.scoobi.io.sequence.SequenceInput.valueFromSequenceFile
import com.nicta.scoobi.core.DList
import pl.edu.icm.coansys.citations.data.MatchableEntity
import pl.edu.icm.cermine.bibref.parsing.tools.CitationUtils
import pl.edu.icm.coansys.citations.util.AugmentedDList.augmentDList
import pl.edu.icm.cermine.bibref.CRFBibReferenceParser
import pl.edu.icm.coansys.commons.scala.strings
import pl.edu.icm.coansys.commons.java.DiacriticsRemover

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

  def lettersNormaliseTokenise(str: String) =
    normaliseTokenise(strings.lettersOnly(str))

  def digitsNormaliseTokenise(str: String) =
    normaliseTokenise(strings.digitsOnly(str))

  def normaliseTokenise(str: String) =
    tokensFromCermine(DiacriticsRemover.removeDiacritics(str))
      .flatMap {
      tok =>
        if (tok.length <= 3 && tok.forall(_.isUpper))
          None
        else
          Some(tok)
    }
      .filter(_.length > 2)
      .map(_.toLowerCase)

  def readCitationsFromDocumentsFromSeqFiles(uris: List[String], parserModel: String): DList[MatchableEntity] = {
    //implicit val documentConverter = new BytesConverter[DocumentMetadata](_.toByteArray, DocumentMetadata.parseFrom(_))
    implicit val referenceConverter = new BytesConverter[ReferenceMetadata](_.toByteArray, ReferenceMetadata.parseFrom(_))
    implicit val wrapperConverter = new BytesConverter[DocumentWrapper](_.toByteArray, DocumentWrapper.parseFrom(_))
    val refmeta = valueFromSequenceFile[DocumentWrapper](uris)
      .flatMap(_.getDocumentMetadata.getReferenceList)
    if (parserModel != null)
      refmeta.flatMapWithResource(new CRFBibReferenceParser(parserModel) with NoOpClose) {
        case (parser, meta) if !meta.getRawCitationText.isEmpty =>
          Some(MatchableEntity.fromUnparsedReferenceMetadata(parser, meta))
        case _ =>
          None
      }
    else
      refmeta.map(MatchableEntity.fromReferenceMetadata(_))
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

  def safeDiv(i1: Int, i2: Int, alternative: => Double = 0.0): Double =
    if (i2 == 0)
      alternative
    else
      i1.toDouble / i2

  def tokensFromCermine(s: String): List[String] =
    CitationUtils.stringToCitation(s).getTokens.map(_.getText).toList
}
