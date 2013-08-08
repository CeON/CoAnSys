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

package pl.edu.icm.coansys.citations.tools.cermine

import java.io.{FileInputStream, File}
import pl.edu.icm.cermine.bibref.CRFBibReferenceParser
import pl.edu.icm.coansys.citations.tools.cermine.util._
import pl.edu.icm.coansys.citations.util.dataset_readers.findAndCollapseStringName
import org.jdom.output.XMLOutputter
import pl.edu.icm.cermine.bibref.transformers.BibEntryToNLMElementConverter

/**
 * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
 */
object CermineParserExecutor {
  def main(args: Array[String]) {
    val modelFile = args(0)
    val inFile = args(1)
    val outFile = args(2)

    val parser = new CRFBibReferenceParser(new FileInputStream(modelFile))

    val citations = extractMixedCitationsFromXmls(Some(new File(inFile)))
    val citationStrings = citations.map(_.getValue)
    val parsedCitations = citationStrings.map {
      cit =>
        val elem = new BibEntryToNLMElementConverter().convert(parser.parseBibReference(cit))
        findAndCollapseStringName(elem)
        elem
    }

    writeCitationsToXml(new File(outFile), parsedCitations.map(new XMLOutputter().outputString))
  }
}
