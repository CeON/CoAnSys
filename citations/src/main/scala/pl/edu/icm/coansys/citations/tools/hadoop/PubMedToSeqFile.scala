/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */

package pl.edu.icm.coansys.citations.tools.hadoop

import java.io.File
import org.slf4j.LoggerFactory
import io.Source
import pl.edu.icm.coansys.commons.scala.files
import pl.edu.icm.coansys.citations.util.EncapsulatedSequenceFileWriter
import pl.edu.icm.coansys.commons.scala.automatic_resource_management.using

/**
 * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
 */
object PubMedToSeqFile {

  val logger = LoggerFactory.getLogger(PubMedToSeqFile.getClass)

  def main(args: Array[String]) {
    val workDir = args(0)
    val outFile = args(1)
    val extension = "nxml"
    val nlms = files.retrieveFilesByExtension(new File(workDir), extension)
    val writeToSeqFile = EncapsulatedSequenceFileWriter.fromLocal[String, String](outFile)
    val prefixLength = new File(workDir).getAbsolutePath.length + 1
    nlms.par.foreach {
      nlm => try {
        using(Source.fromFile(nlm)) {
          source =>
            val key = nlm.getAbsolutePath.substring(prefixLength)
            val value = source.mkString
            writeToSeqFile.synchronized {
              writeToSeqFile((key, value))
            }
        }
      } catch {
        case ex: Throwable =>
          logger.error("Error while processing " + nlm.getCanonicalPath, ex)
      }
    }
    writeToSeqFile.close()
  }
}
