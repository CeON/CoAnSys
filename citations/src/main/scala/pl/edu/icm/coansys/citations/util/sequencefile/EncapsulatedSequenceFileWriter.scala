/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */

package pl.edu.icm.coansys.citations.util.sequencefile

import org.apache.hadoop.io.SequenceFile
import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.Path
import com.nicta.scoobi.io.sequence.SeqSchema

/**
 * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
 */
class EncapsulatedSequenceFileWriter[K, V](val writer: SequenceFile.Writer)
                                          (implicit keySchema: SeqSchema[K],
                                           valueSchema: SeqSchema[V]) extends (((K, V)) => Unit) {
  def apply(arg: (K, V)) {
    val (key, value) = arg
    keySchema.toWritable(key)
    valueSchema.toWritable(value)
    writer.append(keySchema.toWritable(key), valueSchema.toWritable(value))
  }

  def close() {
    writer.close()
  }
}

/**
 * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
 */
object EncapsulatedSequenceFileWriter {
  def fromUri[K, V](conf: Configuration, uri: String)
                   (implicit keySchema: SeqSchema[K],
                    valueSchema: SeqSchema[V]): EncapsulatedSequenceFileWriter[K, V] = {
    val path: Path = new Path(uri)

    val writer = SequenceFile.createWriter(conf, SequenceFile.Writer.file(path),
      SequenceFile.Writer.keyClass(keySchema.mf.erasure), SequenceFile.Writer.valueClass(valueSchema.mf.erasure))
    new EncapsulatedSequenceFileWriter[K, V](writer)
  }

  def fromLocal[K, V](uri: String)
                     (implicit keySchema: SeqSchema[K],
                      valueSchema: SeqSchema[V]): EncapsulatedSequenceFileWriter[K, V] =
    fromUri(new Configuration(), uri)
}