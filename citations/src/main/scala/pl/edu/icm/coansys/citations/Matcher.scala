package pl.edu.icm.coansys.citations

import collection.JavaConversions._
import com.nicta.scoobi.application.ScoobiApp
import com.nicta.scoobi.core.{Emitter, DoFn, DList}
import com.nicta.scoobi.Persist._
import com.nicta.scoobi.InputsOutputs._
import pl.edu.icm.coansys.importers.models.DocumentProtos.DocumentMetadata
import pl.edu.icm.coansys.commons.scala.strings
import pl.edu.icm.coansys.importers.models.DocumentProtosWrapper.DocumentWrapper

/**
 * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
 */
object Matcher extends ScoobiApp {
  override def upload = false

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
  def approximatelyMatchingDocuments(citation: CitationWrapper, index: AuthorIndex): Iterable[DocumentMetadataWrapper] = {
    val documentsWithMatchNo =
      citation.normalisedAuthorTokens
        .flatMap {
        tok => index.getDocumentsByAuthor(tok)
      }
        .groupBy(identity)
        .map {
        case (doc, iterable) => (doc, iterable.size)
      }

    val maxMatchNo =
      if (!documentsWithMatchNo.isEmpty)
        documentsWithMatchNo.values.max
      else
        0
    documentsWithMatchNo.filter {
      case (doc, matchNo) => matchNo >= maxMatchNo - 1
    }.keys
  }

  def approximatelyMatchingDocuments(citation: CitationWrapper, indexUri: String): Iterable[DocumentMetadataWrapper] = {
    approximatelyMatchingDocuments(citation, new AuthorIndex(indexUri))
  }

  def similarity(citation: CitationWrapper, document: DocumentMetadataWrapper): Double = {
    // that's just mock implementation
    val citationString = if (citation.meta.hasTitle) citation.meta.getTitle else citation.meta.getText
    val lcsLen = strings.lcs(citationString, document.meta.getTitle).length
    val minLen = math.min(citation.meta.getTitle.length, document.meta.getTitle.length)
    lcsLen.toDouble / minLen
  }

  def matches(citations: DList[CitationWrapper], indexUri: String) = {
    citations
      .parallelDo(new DoFn[CitationWrapper, (CitationWrapper, DocumentMetadataWrapper)] {
      var index: AuthorIndex = null

      def setup() {
        index = new AuthorIndex(indexUri)
      }

      def process(cit: CitationWrapper, emitter: Emitter[(CitationWrapper, DocumentMetadataWrapper)]) {
        Stream.continually(cit) zip approximatelyMatchingDocuments(cit, index) foreach (emitter.emit(_))
      }

      def cleanup(emitter: Emitter[(CitationWrapper, DocumentMetadataWrapper)]) {}
    })
      .groupByKey[CitationWrapper, DocumentMetadataWrapper]
      .flatMap {
      case (cit, docs) =>
        val aboveThreshold =
          docs
            .map {
            doc => (doc, similarity(cit, doc))
          }
            .filter(_._2 >= minimalSimilarity)

        if (!aboveThreshold.isEmpty)
          Some(cit, aboveThreshold.maxBy(_._2)._1)
        else
          None
    }
      .map {
      case (cit, doc) => (cit.meta.getSource, doc.meta.getKey)
    }
  }

  def readCitationsFromSeqFiles(uris: List[String]): DList[CitationWrapper] = {
    implicit val converter = new BytesConverter[DocumentMetadata](_.toByteArray, DocumentMetadata.parseFrom(_))
    convertValueFromSequenceFile[DocumentMetadata](uris)
      .flatMap(_.getReferenceList.toIterable)
      .map(new CitationWrapper(_))
  }

  def readCitationsFromDocumentsFromSeqFiles(uris: List[String]): DList[CitationWrapper] = {
    implicit val documentConverter = new BytesConverter[DocumentMetadata](_.toByteArray, DocumentMetadata.parseFrom(_))
    implicit val wrapperConverter = new BytesConverter[DocumentWrapper](_.toByteArray, DocumentWrapper.parseFrom(_))
    convertValueFromSequenceFile[DocumentWrapper](uris)
      .flatMap {
      wrapper => DocumentMetadata.parseFrom(wrapper.getMproto).getReferenceList
    }
      .map(new CitationWrapper(_))
  }

  def run() {
    val myMatches = matches(readCitationsFromDocumentsFromSeqFiles(List(args(1))), args(0))
    persist(convertToSequenceFile(myMatches, args(2)))
  }
}
