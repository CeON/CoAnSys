/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */

package pl.edu.icm.coansys.citations.tools.cermine

import java.io.{FileInputStream, File}
import pl.edu.icm.cermine.bibref.CRFBibReferenceParser
import pl.edu.icm.coansys.citations.tools.cermine.util._
import pl.edu.icm.cermine.bibref.parsing.tools.CitationUtils

/**
 * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
 */
object CermineParserExecutor {
  def main() {
    val modelFile = ""
    val inFile = ""
    val outFile = ""

    val parser = new CRFBibReferenceParser(new FileInputStream(modelFile))

    val citations = extractMixedCitationsFromXmls(Some(new File(inFile)))
    val citationStrings = citations.map(_.getValue)
    val parsedCitations = citationStrings.map(cit => CitationUtils.bibEntryToNLM(parser.parseBibReference(cit)))

    writeCitationsToXml(new File(outFile), parsedCitations)
  }
}
