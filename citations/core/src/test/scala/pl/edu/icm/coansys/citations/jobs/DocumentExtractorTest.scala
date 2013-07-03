package pl.edu.icm.coansys.citations.jobs

import org.testng.annotations.Test
import com.nicta.scoobi.application.InMemoryHadoop
import pl.edu.icm.coansys.models.DocumentProtos.{BasicMetadata, DocumentMetadata, DocumentWrapper}
import com.nicta.scoobi.Scoobi._
import org.testng.Assert._
import pl.edu.icm.coansys.citations.data.WireFormats._

/**
 * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
 */
class DocumentExtractorTest extends InMemoryHadoop {
  def scoobiArgs = Seq()
  implicit val conf = new ScoobiConfiguration

  @Test(groups = Array("slow"))
  def basicTest() {
    val wrapperBuilder = DocumentWrapper.newBuilder()
    wrapperBuilder.setRowId("1")
    val docBuilder = DocumentMetadata.newBuilder()
    docBuilder.setKey("1")
    val metaBuilder = BasicMetadata.newBuilder()
    metaBuilder.setJournal("Some journal")
    docBuilder.setBasicMetadata(metaBuilder)
    wrapperBuilder.setDocumentMetadata(docBuilder)
    inMemory {
      val in = DList(wrapperBuilder.build())
      val extracted = persist(DocumentExtractor.extractDocuments(in).materialise).asInstanceOf[Seq[(String, BasicMetadata)]]
      val elem = extracted(0)
      assertEquals(elem._1, "doc_1")
      assertEquals(elem._2.getJournal, "Some journal")
    }
  }
}
