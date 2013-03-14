/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */

package pl.edu.icm.coansys.citations.util

import com.nicta.scoobi.io.sequence.SeqSchema
import org.apache.hadoop.io.SequenceFile
import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.Path

/**
 * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
 */
class SequenceFileIterator[K, V](reader: SequenceFile.Reader)
                                (implicit keySchema: SeqSchema[K], valueSchema: SeqSchema[V]) extends Iterator[(K, V)] {
  assert(reader.getKeyClass == keySchema.mf.erasure, "SequenceFile key type doesn't match SeqSchema")
  assert(reader.getValueClass == valueSchema.mf.erasure, "SequenceFile value type doesn't match SeqSchema")
  private val keyWritable = keySchema.mf.erasure.newInstance().asInstanceOf[keySchema.SeqType]
  private val valueWritable = valueSchema.mf.erasure.newInstance().asInstanceOf[valueSchema.SeqType]
  //TODO: That will fail for empty SequenceFlie
  private var hasMore = true

  def hasNext = hasMore

  def next() = {
    hasMore = reader.next(keyWritable, valueWritable)
    (keySchema.fromWritable(keyWritable), valueSchema.fromWritable(valueWritable))
  }

  def close() {
    reader.close()
  }
}

object SequenceFileIterator {
  def fromUri[K, V](conf: Configuration, uri: String)(implicit keySchema: SeqSchema[K], valueSchema: SeqSchema[V]): SequenceFileIterator[K, V] = {
    val reader = new SequenceFile.Reader(conf, SequenceFile.Reader.file(new Path(uri)))
    new SequenceFileIterator[K, V](reader)
  }
}
