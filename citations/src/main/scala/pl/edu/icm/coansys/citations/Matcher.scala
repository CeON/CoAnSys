package pl.edu.icm.coansys.citations

import collection.JavaConversions._
import com.nicta.scoobi.application.ScoobiApp
import com.nicta.scoobi.core.{Emitter, DoFn, DList}
import com.nicta.scoobi.Persist._
import com.nicta.scoobi.InputsOutputs._
import pl.edu.icm.coansys.importers.models.DocumentProtos.DocumentMetadata
import pl.edu.icm.coansys.importers.models.PICProtos
import pl.edu.icm.coansys.importers.models.DocumentProtosWrapper.DocumentWrapper
import org.apache.hadoop.io.{Text, BytesWritable}
import pl.edu.icm.coansys.importers.models.PICProtos.PicOut

/**
 * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
 */
object Matcher extends ScoobiApp {
  override def upload = false

  /**
   * Minimal similarity between a citation and a document that is used to filter out weak matches.
   */
  val minimalSimilarity = 0.8

  private implicit val picOutConverter =
    new BytesConverter[PICProtos.PicOut](_.toByteArray, PICProtos.PicOut.parseFrom(_))

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
  def approximatelyMatchingDocuments(citation: CitationWrapper, index: AuthorIndex): Iterable[String] = {
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

  //  def approximatelyMatchingDocuments(citation: CitationWrapper, indexUri: String): Iterable[String] = {
  //    approximatelyMatchingDocuments(citation, new AuthorIndex(indexUri))
  //  }

  def similarity(citation: CitationWrapper, document: DocumentMetadataWrapper): Double = {
    // that's just mock implementation
    type BagOfChars = Map[Char, Int]
    def bagOfChars(s: String): BagOfChars =
      s.filterNot(_.isWhitespace).groupBy(identity).mapValues(_.length)
    def commonCharsCount(b1: BagOfChars, b2: BagOfChars) =
      (b1.keySet intersect b2.keySet).map(c => math.min(b1(c), b2(c))).sum
    def charsCount(b: BagOfChars) =
      b.map(_._2).sum

    val citationString = if (citation.meta.hasTitle) citation.meta.getTitle else citation.meta.getText
    //val lcsLen = strings.lcs(citationString, document.meta.getTitle).length
    //val minLen = math.min(citation.meta.getTitle.length, document.meta.getTitle.length)
    //lcsLen.toDouble / minLen
    val citationChars = bagOfChars(citationString)
    val articleTitleChars = bagOfChars(document.meta.getTitle)

    2 * commonCharsCount(citationChars, articleTitleChars).toDouble /
      (charsCount(citationChars) + charsCount(articleTitleChars))
  }

  def citationsWithHeuristic(citations: DList[CitationWrapper], indexUri: String) =
    citations
      .parallelDo(new DoFn[CitationWrapper, (CitationWrapper, String)] {
      var index: AuthorIndex = null

      def setup() {
        index = new AuthorIndex(indexUri)
      }

      def process(cit: CitationWrapper, emitter: Emitter[(CitationWrapper, String)]) {
        Stream.continually(cit) zip approximatelyMatchingDocuments(cit, index) foreach (emitter.emit(_))
      }

      def cleanup(emitter: Emitter[(CitationWrapper, String)]) {
        index.close()
      }
    })


  def matches(citations: DList[CitationWrapper], keyIndexUri: String, authorIndexUri: String) = {
    citationsWithHeuristic(citations, authorIndexUri)
      .parallelDo(new DoFn[(CitationWrapper, String), (CitationWrapper, DocumentMetadataWrapper)] {
      var index: SimpleIndex[Text, BytesWritable] = null
      val text: Text = new Text()

      def setup() {
        index = new SimpleIndex[Text, BytesWritable](keyIndexUri)
      }

      def process(input: (CitationWrapper, String), emitter: Emitter[(CitationWrapper, DocumentMetadataWrapper)]) {
        text.set(input._2)
        index.get(text) match {
          case Some(bytes) =>
            emitter.emit((input._1, DocumentMetadata.parseFrom(bytes.copyBytes)))
          case _ =>
            throw new Exception("No index entry for ---" + input._2 + "---")
        }
      }

      def cleanup(emitter: Emitter[(CitationWrapper, DocumentMetadataWrapper)]) {
        index.close()
      }
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

        if (!aboveThreshold.isEmpty) {
          val target = aboveThreshold.maxBy(_._2)._1
          val sourceUuid = cit.meta.getSource
          val position = cit.meta.getBibRefPosition
          val targetExtId = target.meta.getExtId(0).getValue
          Some(sourceUuid, (position, targetExtId))
        }
        else
          None
    }
      .groupByKey[String, (Int, String)]
      .parallelDo(new DoFn[(String, Iterable[(Int, String)]), (String, PICProtos.PicOut)] {
      var index: SimpleIndex[Text, BytesWritable] = null
      val text: Text = new Text()

      def setup() {
        index = new SimpleIndex[Text, BytesWritable](keyIndexUri)
      }

      def process(input: (String, Iterable[(Int, String)]), emitter: Emitter[(String, PicOut)]) {
        val (sourceUuid, refs) = input
        text.set(sourceUuid)
        index.get(text) match {
          case Some(bytes) =>
            val sourceDoc = DocumentMetadata.parseFrom(bytes.copyBytes())

            val outBuilder = PICProtos.PicOut.newBuilder()
            outBuilder.setDocId(sourceDoc.getExtId(0).getValue)
            for ((position, targetExtId) <- refs) {
              outBuilder.addRefs(PICProtos.References.newBuilder().setDocId(targetExtId).setRefNum(position))
            }

            emitter.emit((sourceUuid, outBuilder.build()))
          case _ =>
            throw new Exception("No index entry for ---" + input._2 + "---")
        }
      }

      def cleanup(emitter: Emitter[(String, PicOut)]) {
        index.close()
      }
    })
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
    configuration.set("mapred.max.split.size", 500000)
    configuration.setMinReducers(48)

    val myMatches = matches(readCitationsFromDocumentsFromSeqFiles(List(args(2))), args(0), args(1))


    persist(convertToSequenceFile(myMatches, args(3)))
  }
}
