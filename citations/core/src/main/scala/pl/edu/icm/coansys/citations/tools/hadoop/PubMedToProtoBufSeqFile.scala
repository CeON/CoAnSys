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
