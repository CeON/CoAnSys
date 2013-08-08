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
  implicit val conf = ScoobiConfiguration()

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
