/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */

package pl.edu.icm.coansys.citations.tools.cermine

import java.io.{FileInputStream, File}
import pl.edu.icm.cermine.bibref.CRFBibReferenceParser
import pl.edu.icm.coansys.citations.tools.cermine.util._
import pl.edu.icm.coansys.citations.util.dataset_readers.findAndCollapseStringName
import pl.edu.icm.cermine.bibref.parsing.tools.CitationUtils
import org.jdom.output.XMLOutputter

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
        val elem = CitationUtils.bibEntryToNLM(parser.parseBibReference(cit))
        findAndCollapseStringName(elem)
        elem
    }

    writeCitationsToXml(new File(outFile), parsedCitations.map(new XMLOutputter().outputString))
  }
}
