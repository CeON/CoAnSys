/*
 * This file is part of CoAnSys project.
 * Copyright (c) 20012-2013 ICM-UW
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
import org.slf4j.LoggerFactory
import io.Source
import pl.edu.icm.coansys.commons.scala.files
import pl.edu.icm.coansys.commons.scala.automatic_resource_management.using
import pl.edu.icm.coansys.citations.util.sequencefile.ConvertingSequenceFileWriter

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
    val writeToSeqFile = ConvertingSequenceFileWriter.fromLocal[String, String](outFile)
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
