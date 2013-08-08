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
