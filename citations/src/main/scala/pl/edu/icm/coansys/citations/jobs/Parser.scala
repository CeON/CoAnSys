/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */

package pl.edu.icm.coansys.citations.jobs

import com.nicta.scoobi.Scoobi._
import pl.edu.icm.coansys.citations.util.misc._
import pl.edu.icm.coansys.citations.util.AugmentedDList._
import pl.edu.icm.cermine.bibref.CRFBibReferenceParser
import pl.edu.icm.cermine.bibref.parsing.tools.CitationUtils
import pl.edu.icm.coansys.citations.util.NoOpClose
import org.jdom.output.XMLOutputter

/**
 * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
 */
object Parser extends ScoobiApp {
  override def upload = false

  def run() {
    configuration.set("mapred.max.split.size", 500000)
    configuration.setMinReducers(4)

    val modelFile = "/pl/edu/icm/cermine/bibref/acrf-small.ser.gz"

    val citations = readCitationsFromDocumentsFromSeqFiles(List(args(0)))
    val parsedCitations = citations.
      mapWithResource(new CRFBibReferenceParser(this.getClass.getResourceAsStream(modelFile)) with NoOpClose) {
      case (parser, citation) =>
        val parsed = parser.parseBibReference(citation.meta.getRawCitationText)
        val nlm = CitationUtils.bibEntryToNLM(parsed)
        new XMLOutputter().outputString(nlm)
    }
    persist(toTextFile(parsedCitations, args(1), overwrite = true))
  }
}
