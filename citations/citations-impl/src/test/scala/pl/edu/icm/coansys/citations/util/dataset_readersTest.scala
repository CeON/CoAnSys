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

import org.testng.Assert._
import org.testng.annotations.Test
import pl.edu.icm.coansys.citations.util.dataset_readers._
import org.jdom.input.SAXBuilder
import org.apache.commons.io.IOUtils
import org.jdom.output.XMLOutputter
import org.custommonkey.xmlunit.XMLAssert._
import pl.edu.icm.cermine.bibref.model.BibEntry

/**
 * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
 */
class dataset_readersTest {
  @Test(groups = Array("fast"))
  def findAndCollapseStringNameTest() {
    def getRootElement(xml: String) =
      new SAXBuilder("org.apache.xerces.parsers.SAXParser").build(IOUtils.toInputStream(xml)).getDocument.getRootElement
    val outputter = new XMLOutputter()

    val tests = List(
      ("<tag></tag>", "<tag></tag>"),
      ("<tag><string-name>test</string-name></tag>", "<tag><string-name><surname>test</surname></string-name></tag>"),
      ("<tag><string-name>Jan Kowalski</string-name></tag>",
        "<tag><string-name><surname>Jan Kowalski</surname></string-name></tag>"),
      ("<tag><string-name><given-names>Jan</given-names> <surname>Kowalski</surname></string-name></tag>",
        "<tag><string-name><surname>Jan Kowalski</surname></string-name></tag>"))

    for ((in, out) <- tests) {
      val root = getRootElement(in)
      findAndCollapseStringName(root)
      assertXMLEqual(outputter.outputString(root), out)
    }
  }

  @Test(groups = Array("fast"))
  def taggedReferenceToBibEntryTest() {
    val bib1 = taggedReferenceToBibEntry("Jan Kowalski", Map())
    val bib2 = taggedReferenceToBibEntry("<author>Jan Kowalski</author>", Map())
    val bib3 = taggedReferenceToBibEntry("<author>Jan Kowalski</author>", Map("author" -> BibEntry.FIELD_AUTHOR))

    assertTrue(bib1.getFieldKeys.isEmpty)
    assertTrue(bib2.getFieldKeys.isEmpty)
    assertEquals(bib3.getAllFieldValues(BibEntry.FIELD_AUTHOR).get(0), "Jan Kowalski")

    assertEquals(bib1.getText, "Jan Kowalski")
    assertEquals(bib2.getText, "Jan Kowalski")
    assertEquals(bib3.getText, "Jan Kowalski")
  }
}
