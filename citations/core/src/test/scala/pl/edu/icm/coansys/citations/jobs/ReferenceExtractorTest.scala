package pl.edu.icm.coansys.citations.jobs

import org.testng.annotations.Test
import com.nicta.scoobi.application.InMemoryHadoop
import pl.edu.icm.coansys.importers.models.DocumentProtos.{ReferenceMetadata, BasicMetadata, DocumentMetadata, DocumentWrapper}
import com.nicta.scoobi.Scoobi._
import org.testng.Assert._
import pl.edu.icm.coansys.citations.data.WireFormats._

/**
 * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
 */
class ReferenceExtractorTest extends InMemoryHadoop {
  def scoobiArgs = Seq()
  implicit val conf = new ScoobiConfiguration

  @Test(groups = Array("slow"))
  def basicTest() {
    val wrapperBuilder = DocumentWrapper.newBuilder()
    wrapperBuilder.setRowId("20")
    val docBuilder = DocumentMetadata.newBuilder()
    docBuilder.setKey("20")
    val metaBuilder = BasicMetadata.newBuilder()
    docBuilder.setBasicMetadata(metaBuilder)
    docBuilder.addReference(
      ReferenceMetadata.newBuilder()
        .setSourceDocKey("20")
        .setPosition(1)
        .setBasicMetadata(BasicMetadata.newBuilder()
          .setJournal("Another journal")))


    wrapperBuilder.setDocumentMetadata(docBuilder)
    inMemory {
      val in = DList(wrapperBuilder.build())
      val extracted = persist(ReferenceExtractor.extractReferences(in).materialise).asInstanceOf[Seq[(String, BasicMetadata)]]
      val elem = extracted(0)
      assertEquals(elem._1, "cit_20_1")
      assertEquals(elem._2.getJournal, "Another journal")
    }
  }
}
