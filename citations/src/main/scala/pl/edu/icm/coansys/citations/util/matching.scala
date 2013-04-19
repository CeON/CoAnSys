/*
 * (C) 2010-2012 ICM UW. All rights reserved.
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
