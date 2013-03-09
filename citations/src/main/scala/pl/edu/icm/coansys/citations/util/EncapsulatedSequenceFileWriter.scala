/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */

package pl.edu.icm.coansys.citations.util

import org.apache.hadoop.io.SequenceFile
import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.Path
import pl.edu.icm.coansys.citations.util.EncapsulatedSequenceFileWriter.WritablePreparer

/**
 * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
 */
class EncapsulatedSequenceFileWriter[Kw, Vw, Ka, Va](val writer: SequenceFile.Writer)
                                                    (implicit keyPreperer: WritablePreparer[Ka, Kw],
                                                     valuerPreparer: WritablePreparer[Va, Vw],
                                                     m1: Manifest[Kw], m2: Manifest[Vw]) extends (((Ka, Va)) => Unit) {
  private val keyWritable = manifest[Kw].erasure.newInstance().asInstanceOf[Kw]
  private val valueWritable = manifest[Vw].erasure.newInstance().asInstanceOf[Vw]

  def apply(arg: (Ka, Va)) {
    val (key, value) = arg
    keyPreperer.prepare(key, keyWritable)
    valuerPreparer.prepare(value, valueWritable)
    writer.append(keyWritable, valueWritable)
  }

  def close() {
    writer.close()
  }
}

/**
 * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
 */
object EncapsulatedSequenceFileWriter {

  trait WritablePreparer[In, Out] {
    def prepare(in: In, out: Out)
  }

  def fromLocal[Kw, Vw, Ka, Va](uri: String)
                               (implicit keyPreperer: WritablePreparer[Ka, Kw],
                                valuerPreparer: WritablePreparer[Va, Vw],
                                m1: Manifest[Kw], m2: Manifest[Vw]): EncapsulatedSequenceFileWriter[Kw, Vw, Ka, Va] = {
    val conf: Configuration = new Configuration
    val path: Path = new Path(uri)

    val writer = SequenceFile.createWriter(conf, SequenceFile.Writer.file(path),
      SequenceFile.Writer.keyClass(manifest[Kw].erasure), SequenceFile.Writer.valueClass(manifest[Vw].erasure))
    new EncapsulatedSequenceFileWriter[Kw, Vw, Ka, Va](writer)
  }
}