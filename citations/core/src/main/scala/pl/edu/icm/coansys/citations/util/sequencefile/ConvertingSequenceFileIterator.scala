/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */

package pl.edu.icm.coansys.citations.util.sequencefile

import com.nicta.scoobi.io.sequence.SeqSchema
import org.apache.hadoop.io.SequenceFile
import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.Path

/**
 * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
 */
class ConvertingSequenceFileIterator[K, V](iterator: SequenceFileIterator)
                                          (implicit keySchema: SeqSchema[K], valueSchema: SeqSchema[V]) extends Iterator[(K, V)] {
  def hasNext = iterator.hasNext

  def next() = {
    val (key, value) = iterator.next()
    (keySchema.fromWritable(key.asInstanceOf[keySchema.SeqType]),
      valueSchema.fromWritable(value.asInstanceOf[valueSchema.SeqType]))
  }

  def close() {
    iterator.close()
  }
}

object ConvertingSequenceFileIterator {
  def fromUri[K, V](conf: Configuration, uri: String)(implicit keySchema: SeqSchema[K], valueSchema: SeqSchema[V]): ConvertingSequenceFileIterator[K, V] = {
    val reader = new SequenceFile.Reader(conf, SequenceFile.Reader.file(new Path(uri)))
    assert(reader.getKeyClass == keySchema.mf.erasure, "SequenceFile key type doesn't match SeqSchema")
    assert(reader.getValueClass == valueSchema.mf.erasure, "SequenceFile value type doesn't match SeqSchema")
    new ConvertingSequenceFileIterator[K, V](new SequenceFileIterator(reader))
  }

  def fromLocal[K, V](uri: String)
                     (implicit keySchema: SeqSchema[K],
                      valueSchema: SeqSchema[V]): ConvertingSequenceFileIterator[K, V] =
    fromUri(new Configuration(), uri)
}
