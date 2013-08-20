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

package pl.edu.icm.coansys.citations.util

import pl.edu.icm.coansys.citations.data.MatchableEntity
import pl.edu.icm.coansys.citations.indices.AuthorIndex

/**
 * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
 */
object matching {
  type EntityId = String

  /**
   * A heuristics to retrieve documents that are most probable to be associated with a given citation.
   *
   * Algorithm:
   * 1.	Retrieve all documents which contain at least one non-exactly matching author.
   * 2.	Let M=maximum number of matching authors.
   * 3.	Filter out documents containing less than M-1 matching authors.
   *
   * @param citation a citation to process
   * @param index an index to be used for document retrieval
   * @return matching documents
   */
  def approximatelyMatchingDocuments(citation: MatchableEntity, index: AuthorIndex): Iterable[EntityId] = {
    val documentsWithMatchNo =
      citation.normalisedAuthorTokens.toList
        .flatMap(tok => index.getDocumentsByAuthor(tok).filterNot(_ == citation.id))
        .groupBy(identity)
        .map {
        case (doc, iterable) => (doc, iterable.size)
      }

    val maxMatchNo =
      if (!documentsWithMatchNo.isEmpty)
        documentsWithMatchNo.values.max
      else
        0
    val minMatchNo = math.max(maxMatchNo - 1, citation.normalisedAuthorTokens.size.toDouble * 2 / 3)
    documentsWithMatchNo.filter {
      case (doc, matchNo) => matchNo >= minMatchNo
    }.keys
  }

  def approximatelyMatchingDocumentsStats(citation: MatchableEntity, index: AuthorIndex) = {
    val documentsWithMatchNo =
      citation.normalisedAuthorTokens
        .flatMap(tok => index.getDocumentsByAuthor(tok).filterNot(_ == citation.id))
        .groupBy(identity)
        .map {
        case (doc, iterable) => (doc, iterable.size)
      }

    val maxMatchNo =
      if (!documentsWithMatchNo.isEmpty)
        documentsWithMatchNo.values.max
      else
        0
    val minMatchNo = math.max(maxMatchNo - 1, citation.normalisedAuthorTokens.size.toDouble * 2 / 3)
    val docs = documentsWithMatchNo.filter {
      case (doc, matchNo) => matchNo >= minMatchNo
    }.keys

    ('"' + citation.toReferenceString.filterNot(_ == '"') + '"', citation.normalisedAuthorTokens.mkString(" "), docs.size, maxMatchNo)
  }
}
