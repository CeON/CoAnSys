package pl.edu.icm.coansys.citations

import collection.JavaConversions._
import com.nicta.scoobi.application.ScoobiApp
import com.nicta.scoobi.core.DList
import com.nicta.scoobi.Persist._
import com.nicta.scoobi.InputsOutputs._
import pl.edu.icm.coansys.importers.models.DocumentProtos.DocumentMetadata
import pl.edu.icm.coansys.commons.scala.strings

/**
 * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
 */
object Matcher extends ScoobiApp {
  /**
   * Minimal similarity between a citation and a document that is used to filter out weak matches.
   */
  val minimalSimilarity = 0.5

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
  def approximatelyMatchingDocuments(citation: CitationWrapper, index: AuthorIndex) = {
    val documentsWithMatchNo =
      citation.normalisedAuthorTokens
        .flatMap {
        tok => index.getDocumentsByAuthor(tok)
      }
        .groupBy(identity)
        .map {
        case (doc, iterable) => (doc, iterable.size)
      }
    val maxMatchNo = documentsWithMatchNo.values.max
    documentsWithMatchNo.filter {
      case (doc, matchNo) => matchNo >= maxMatchNo - 1
    }.keys
  }

  def similarity(citation: CitationWrapper, document: DocumentMetadataWrapper): Double = {
    // that's just mock implementation
    val lcsLen = strings.lcs(citation.meta.getTitle, document.meta.getTitle).length
    val minLen = math.min(citation.meta.getTitle.length, document.meta.getTitle.length)
    lcsLen.toDouble / minLen
  }

  def matches(citations: DList[CitationWrapper], index: AuthorIndex) = {
    citations
      .flatMap {
      cit => Stream.continually(cit) zip approximatelyMatchingDocuments(cit, index)
    }
      .groupByKey[CitationWrapper, DocumentMetadataWrapper]
      .flatMap {
      case (cit, docs) =>
        val best = docs.map {
          doc => (doc, similarity(cit, doc))
        }.maxBy(_._2)
        //Return a match only if its certainty is greater than a limit
        if (best._2 >= minimalSimilarity)
          Some(cit, best._1)
        else
          None
    }
  }

  def readCitationsFromSeqFiles(uris: List[String]): DList[CitationWrapper] = {
    implicit val converter = new BytesConverter[DocumentMetadata](_.toByteArray, DocumentMetadata.parseFrom(_))
    convertValueFromSequenceFile[DocumentMetadata](uris)
      .flatMap(_.getReferenceList.toIterable)
      .map(new CitationWrapper(_))
  }

  def run() {
    persist(
      convertToSequenceFile(matches(readCitationsFromSeqFiles(List(args(0))), new AuthorIndex(args(0))), args(1)))
  }
}
