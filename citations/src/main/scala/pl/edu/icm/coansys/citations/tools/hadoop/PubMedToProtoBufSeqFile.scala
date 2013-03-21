/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */

package pl.edu.icm.coansys.citations.tools.hadoop

import java.io.File
import org.apache.hadoop.io.BytesWritable
import pl.edu.icm.coansys.citations.util.misc
import pl.edu.icm.coansys.citations.util.nlm.pubmedNlmToProtoBuf
import org.slf4j.LoggerFactory
import pl.edu.icm.coansys.commons.scala.files
import scala.Array
import com.nicta.scoobi.io.sequence.SeqSchema
import pl.edu.icm.coansys.citations.util.sequencefile.ConvertingSequenceFileWriter

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
    implicit val _ = new SeqSchema[Array[Byte]] {
      type SeqType = BytesWritable

      def toWritable(x: Array[Byte]) = new BytesWritable(x, x.length)

      def fromWritable(x: this.type#SeqType) = x.copyBytes()

      val mf = manifest[BytesWritable]
    }
    val writeToSeqFile =
      ConvertingSequenceFileWriter.fromLocal[Array[Byte], Array[Byte]](outFile)
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
