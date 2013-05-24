package pl.edu.icm.coansys.citations.jobs

import com.nicta.scoobi.Scoobi._
import pl.edu.icm.coansys.importers.models.DocumentProtos.DocumentWrapper
import pl.edu.icm.coansys.citations.data.WireFormats._
import java.io.File

/**
 * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
 */


object DocumentExtractor extends ScoobiApp {
  val myJars = System.getProperty("java.class.path").split(File.pathSeparator)
    .filter(_.toLowerCase.contains(File.separator + "distcache" + File.separator))
    .filter(_.toLowerCase.endsWith(".jar"))

  configuration.addJars(myJars)

  lazy val documentIdPrefix = "doc_"

  def extractDocuments(in: DList[DocumentWrapper]) =
    in.filterNot(_.getDocumentMetadata.getKey.isEmpty)
      .map(x => (documentIdPrefix + x.getDocumentMetadata.getKey, x.getDocumentMetadata.getBasicMetadata))

  def run() {
    val inUri = args(0)
    val outUri = args(1)

    val entities = extractDocuments(convertValueFromSequenceFile[DocumentWrapper](inUri))
    persist(convertToSequenceFile(entities, outUri))
  }
}
