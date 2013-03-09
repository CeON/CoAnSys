/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */

package pl.edu.icm.coansys.citations.tools.hadoop

import java.io.File
import org.apache.hadoop.io.Text
import org.slf4j.LoggerFactory
import io.Source
import pl.edu.icm.coansys.commons.scala.files
import pl.edu.icm.coansys.citations.util.EncapsulatedSequenceFileWriter.WritablePreparer
import pl.edu.icm.coansys.citations.util.EncapsulatedSequenceFileWriter

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
    implicit val _ = new WritablePreparer[String, Text] {
      def prepare(in: String, out: Text) {
        out.set(in)
      }
    }
    val writeToSeqFile = EncapsulatedSequenceFileWriter.fromLocal[Text, Text, String, String](outFile)
    val prefixLength = new File(workDir).getAbsolutePath.length + 1
    nlms.par.foreach {
      nlm => try {
        val key = nlm.getAbsolutePath.substring(prefixLength)
        val value = Source.fromFile(nlm).mkString
        writeToSeqFile.synchronized {
          writeToSeqFile((key, value))
        }
      } catch {
        case ex: Throwable =>
          logger.error("Error while processing " + nlm.getCanonicalPath, ex)
      }
    }
    writeToSeqFile.close()
  }
}
