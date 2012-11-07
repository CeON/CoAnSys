package pl.edu.icm.coansys.citations

import com.nicta.scoobi.application.ScoobiApp
import com.nicta.scoobi.core.DList
import pl.edu.icm.coansys.importers.models.DocumentProtos._
import pl.edu.icm.coansys.importers.models.DocumentProtosWrapper._
import com.nicta.scoobi.InputsOutputs.convertValueFromSequenceFile

/**
 * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
 */
object IndexBuilder extends ScoobiApp {
  override def upload = false

  def doMatching() {
    throw new RuntimeException
  }

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
    println(args)
    ApproximateIndex.buildAuthorIndex(readDocsFromSeqFiles(List(args(0))), args(1))
    //ApproximateIndex.buildAuthorIndex(mockReadDocs(), args(0))
  }
}
