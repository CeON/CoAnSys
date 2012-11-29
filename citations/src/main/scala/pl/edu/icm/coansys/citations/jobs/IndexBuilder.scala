package pl.edu.icm.coansys.citations.jobs

import com.nicta.scoobi.application.ScoobiApp
import com.nicta.scoobi.core.DList
import pl.edu.icm.coansys.importers.models.DocumentProtos._
import pl.edu.icm.coansys.importers.models.DocumentProtosWrapper._
import com.nicta.scoobi.InputsOutputs.convertValueFromSequenceFile
import pl.edu.icm.coansys.citations.util.BytesConverter
import pl.edu.icm.coansys.citations.data.DocumentMetadataWrapper
import pl.edu.icm.coansys.citations.indices.{SimpleIndex, ApproximateIndex}

/**
 * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
 */
object IndexBuilder extends ScoobiApp {
  override def upload = false

  def mockReadDocs(): DList[DocumentMetadataWrapper] = DList.apply[DocumentMetadataWrapper](
    DocumentMetadata.newBuilder().setKey("1").addAuthor(Author.newBuilder().setKey("1").setName("aaa bbb")).build(),
    DocumentMetadata.newBuilder().setKey("2").addAuthor(Author.newBuilder().setKey("2").setName("bab ccc")).build(),
    DocumentMetadata.newBuilder().setKey("3").addAuthor(Author.newBuilder().setKey("3").setName("cc ddd")).build(),
    DocumentMetadata.newBuilder().setKey("4").addAuthor(Author.newBuilder().setKey("4").setName("ddd eee")).build()
  )

  def readDocsFromSeqFiles(uris: List[String]): DList[DocumentMetadataWrapper] = {
    implicit val converter = new BytesConverter[DocumentWrapper](_.toByteArray, DocumentWrapper.parseFrom(_))
    convertValueFromSequenceFile[DocumentWrapper](uris).map {
      wrapper => DocumentMetadata.parseFrom(wrapper.getMproto)
    }
  }

  def run() {
    if (args.length != 3) {
      println("Usage: IndexBuilder [-key|-author] <input_seqfile> <output_index_path>")
    } else {
      args(0) match {
        case "-key" =>
          SimpleIndex.buildKeyIndex(readDocsFromSeqFiles(List(args(1))), args(2))
        case "-author" =>
          ApproximateIndex.buildAuthorIndex(readDocsFromSeqFiles(List(args(1))), args(2))
        case _ =>
          println("Usage: IndexBuilder [-key|-author] <input_seqfile> <output_index_path>")
      }
    }
  }
}
