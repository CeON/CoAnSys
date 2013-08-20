package pl.edu.icm.coansys.citations.jobs.auxiliary

import collection.JavaConversions._
import com.nicta.scoobi.Scoobi._
import pl.edu.icm.coansys.citations.util.MyScoobiApp
import pl.edu.icm.coansys.models.DocumentProtos.DocumentWrapper
import pl.edu.icm.coansys.citations.data.WireFormats._

/**
 * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
 */
object CsvForAnalystsGenerator extends MyScoobiApp {
  def enquote(s: String) =
    '"' + s.split(raw"\s+").mkString(" ").replaceAll("\"", "\"\"") + '"'

  def run() {
    val documentsUrl = args(0)
    val matchingUrl = args(1)
    val outUrl = args(2)

    val matching = fromSequenceFile[String, String](matchingUrl)
    val documents = valueFromSequenceFile[DocumentWrapper](documentsUrl)
    val citations = documents.mapFlatten { doc =>
      val journal = doc.getDocumentMetadata.getBasicMetadata.getJournal
      doc.getDocumentMetadata.getReferenceList.map { ref =>
        val id = "cit_" + doc.getRowId + "_" + ref.getPosition
        (id, (journal, ref.getRawCitationText))
      }
    }

    val stage1 = matching.joinRight(citations).map {
      case (citId, (dest, (journal, rawCit))) =>
        val trimmedCit = citId.substring(4)
        val sep = trimmedCit.lastIndexOf("_")
        val srcDoc = trimmedCit.substring(0, sep)
        val pos = trimmedCit.substring(sep + 1)
        val trimmedDest = dest match {
          case Some(d) =>
            val col = d.indexOf(":")
            d.substring(col + 5)
          case None =>
            ""
        }
        (trimmedDest, (journal, srcDoc, pos, rawCit))
    }

    val destDocuments = documents.map {doc =>
      val authors = doc.getDocumentMetadata.getBasicMetadata.getAuthorList.map(
        a => if (a.hasName) a.getName else a.getSurname + ", " + a.getForenames).mkString("; ")
      val title = doc.getDocumentMetadata.getBasicMetadata.getTitleList.map(_.getText).mkString(" ")
      val journal = doc.getDocumentMetadata.getBasicMetadata.getJournal
      val year = doc.getDocumentMetadata.getBasicMetadata.getYear
      (doc.getRowId, (authors, title, journal, year))
    }

    val stage2 = stage1.joinLeft(destDocuments).mapFlatten {
      case (destId, ((srcjournal, srcDoc, pos, rawCit), Some((authors, title, dstjournal, year)))) =>
        Some(List(srcjournal, srcDoc, pos, rawCit, destId, authors, title, dstjournal, year).map(enquote).mkString(","))
      case (destId, ((srcjournal, srcDoc, pos, rawCit), None)) =>
        Some(List(srcjournal, srcDoc, pos, rawCit, destId, "", "", "", "").map(enquote).mkString(","))
    }

    persist(stage2.toTextFile(outUrl, overwrite = true))

  }
}
