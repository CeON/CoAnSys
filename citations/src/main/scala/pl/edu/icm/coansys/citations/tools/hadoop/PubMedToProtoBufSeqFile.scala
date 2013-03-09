/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */

package pl.edu.icm.coansys.citations.tools.hadoop

import java.io.File
import org.apache.hadoop.io.{BytesWritable, SequenceFile}
import pl.edu.icm.coansys.citations.util.misc
import pl.edu.icm.coansys.citations.util.nlm.pubmedNlmToProtoBuf
import org.apache.hadoop.fs.Path
import org.apache.hadoop.conf.Configuration
import org.slf4j.LoggerFactory
import pl.edu.icm.coansys.commons.scala.files

/**
 * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
 */
object PubMedToProtoBufSeqFile {

  val logger = LoggerFactory.getLogger(PubMedToSeqFile.getClass)

  class EncapsulatedSequenceFileWriter(val writer: SequenceFile.Writer) extends (((Array[Byte], Array[Byte])) => Unit) {
    private val keyWritable = new BytesWritable()
    private val valueWritable = new BytesWritable()

    def apply(arg: (Array[Byte], Array[Byte])) {
      val (key, value) = arg
      keyWritable.set(key, 0, key.length)
      valueWritable.set(value, 0, value.length)
      writer.append(keyWritable, valueWritable)
    }

    def close() {
      writer.close()
    }
  }

  object EncapsulatedSequenceFileWriter {
    def fromLocal(uri: String): EncapsulatedSequenceFileWriter = {
      val conf: Configuration = new Configuration
      val path: Path = new Path(uri)

      val writer = SequenceFile.createWriter(conf, SequenceFile.Writer.file(path),
        SequenceFile.Writer.keyClass(classOf[BytesWritable]), SequenceFile.Writer.valueClass(classOf[BytesWritable]))
      new EncapsulatedSequenceFileWriter(writer)
    }
  }

  def main(args: Array[String]) {
    val workDir = args(0)
    val outFile = args(1)
    val extension = "nxml"
    val nlms = files.retrieveFilesByExtension(new File(workDir), extension)
    val writeToSeqFile = EncapsulatedSequenceFileWriter.fromLocal(outFile)
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
