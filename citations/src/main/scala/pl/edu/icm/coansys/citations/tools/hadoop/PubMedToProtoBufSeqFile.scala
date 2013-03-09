/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */

package pl.edu.icm.coansys.citations.tools.hadoop

import java.io.File
import org.apache.hadoop.io.BytesWritable
import pl.edu.icm.coansys.citations.util.{EncapsulatedSequenceFileWriter, misc}
import pl.edu.icm.coansys.citations.util.nlm.pubmedNlmToProtoBuf
import org.slf4j.LoggerFactory
import pl.edu.icm.coansys.commons.scala.files
import scala.Array
import pl.edu.icm.coansys.citations.util.EncapsulatedSequenceFileWriter.WritablePreparer

/**
 * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
 */
object PubMedToProtoBufSeqFile {

  val logger = LoggerFactory.getLogger(PubMedToSeqFile.getClass)

  def main(args: Array[String]) {
    val workDir = args(0)
    val outFile = args(1)
    val extension = "nxml"
    val nlms = files.retrieveFilesByExtension(new File(workDir), extension)
    implicit val _ = new WritablePreparer[Array[Byte], BytesWritable] {
      def prepare(in: Array[Byte], out: BytesWritable) {
        out.set(in, 0, in.length)
      }
    }
    val writeToSeqFile =
      EncapsulatedSequenceFileWriter.fromLocal[BytesWritable, BytesWritable, Array[Byte], Array[Byte]](outFile)
    nlms.par.foreach {
      nlm => try {
        val meta = pubmedNlmToProtoBuf(nlm)
        val key = misc.uuidEncode(meta.getRowId)
        val value = meta.toByteArray
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
