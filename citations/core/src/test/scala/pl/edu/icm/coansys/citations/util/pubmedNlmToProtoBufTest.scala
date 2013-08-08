/*
 * This file is part of CoAnSys project.
 * Copyright (c) 2012-2013 ICM-UW
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

import org.testng.annotations.{BeforeClass, Test}
import org.testng.Assert._
import pl.edu.icm.coansys.citations.util.nlm.pubmedNlmToProtoBuf
import pl.edu.icm.coansys.models.DocumentProtos.DocumentMetadata

/**
 * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
 */
class pubmedNlmToProtoBufTest {
  var doc: DocumentMetadata = null

  @BeforeClass
  def setUp() {
    doc = pubmedNlmToProtoBuf(this.getClass.getResourceAsStream("/pl/edu/icm/coansys/citations/sample.nxml"))
      .getDocumentMetadata
  }

  @Test(groups = Array("fast"))
  def extIdTest() {
    assertEquals(doc.getExtIdCount, 4)
    assertEquals(doc.getExtId(0).getKey, "pmc")
    assertEquals(doc.getExtId(0).getValue, "2751467")
    assertEquals(doc.getExtId(1).getKey, "pmid")
    assertEquals(doc.getExtId(1).getValue, "18446519")
    assertEquals(doc.getExtId(2).getKey, "publisher-id")
    assertEquals(doc.getExtId(2).getValue, "9022")
    assertEquals(doc.getExtId(3).getKey, "doi")
    assertEquals(doc.getExtId(3).getValue, "10.1208/s12248-008-9022-y")
  }

  @Test(groups = Array("fast"))
  def abstractTest() {
    assertEquals(doc.getDocumentAbstractCount, 1)
    assertEquals(doc.getDocumentAbstract(0).getLanguage, "en")
    assertTrue(doc.getDocumentAbstract(0).getText.startsWith(
      """SLC5A8 and SLC5A12 are sodium-coupled monocarboxylate transporters (SMCTs), the former"""))
  }

  @Test(groups = Array("fast"))
  def doiTest() {
    assertEquals(doc.getBasicMetadata.getDoi, "10.1208/s12248-008-9022-y")
  }

  @Test(groups = Array("fast"))
  def pagesTest() {
    assertEquals(doc.getBasicMetadata.getPages, "193-199")
  }

  @Test(groups = Array("fast"))
  def volumeTest() {
    assertEquals(doc.getBasicMetadata.getVolume, "10")
  }

  @Test(groups = Array("fast"))
  def issueTest() {
    assertEquals(doc.getBasicMetadata.getIssue, "1")
  }

  @Test(groups = Array("fast"))
  def yearTest() {
    assertEquals(doc.getBasicMetadata.getYear, "2008")
  }

  @Test(groups = Array("fast"))
  def journalTest() {
    assertEquals(doc.getBasicMetadata.getJournal, "The AAPS Journal")
  }

  @Test(groups = Array("fast"))
  def titleTest() {
    assertEquals(doc.getBasicMetadata.getTitle(0).getText, "Sodium-coupled Monocarboxylate Transporters in Normal Tissues and in Cancer")
  }

  @Test(groups = Array("fast"))
  def authorTest() {
    assertEquals(doc.getBasicMetadata.getAuthorCount, 7)
    assertEquals(doc.getBasicMetadata.getAuthor(0).getSurname, "Ganapathy")
    assertEquals(doc.getBasicMetadata.getAuthor(0).getForenames, "Vadivel")
  }

  @Test(groups = Array("fast"))
  def refTest() {
    assertEquals(doc.getReferenceCount, 35)
    val ref = doc.getReference(0)
    assertEquals(ref.getPosition, 1)
    assertEquals(ref.getBasicMetadata.getAuthorCount, 2)
    assertEquals(ref.getBasicMetadata.getAuthor(0).getSurname, "Enerson")
    assertEquals(ref.getBasicMetadata.getAuthor(0).getForenames, "B. E.")
    assertEquals(ref.getBasicMetadata.getAuthor(1).getSurname, "Drewes")
    assertEquals(ref.getBasicMetadata.getAuthor(1).getForenames, "L. R.")
    assertEquals(ref.getBasicMetadata.getTitle(0).getText,
      "Molecular features, regulation, and function of monocarboxylate transporters: implications for drug delivery")
    assertEquals(ref.getBasicMetadata.getJournal, "J. Pharm. Sci")
    assertEquals(ref.getBasicMetadata.getYear, "2003")
    assertEquals(ref.getBasicMetadata.getVolume, "92")
    assertEquals(ref.getBasicMetadata.getPages, "1531-1544")
    assertEquals(ref.getBasicMetadata.getDoi, "10.1002/jps.10389")
  }
}
