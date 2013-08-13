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

package pl.edu.icm.coansys.citations.util.sequencefile

import org.apache.hadoop.io.SequenceFile
import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.Path
import com.nicta.scoobi.io.sequence.SeqSchema

/**
 * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
 */
class ConvertingSequenceFileWriter[K, V](val writer: SequenceFile.Writer)
                                        (implicit keySchema: SeqSchema[K],
                                         valueSchema: SeqSchema[V]) extends (((K, V)) => Unit) {
  def apply(arg: (K, V)) {
    append(arg._1, arg._2)
  }

  def append(key: K, value: V) {
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
object ConvertingSequenceFileWriter {
  def fromUri[K, V](conf: Configuration, uri: String)
                   (implicit keySchema: SeqSchema[K],
                    valueSchema: SeqSchema[V]): ConvertingSequenceFileWriter[K, V] = {
    val path: Path = new Path(uri)

    val writer = SequenceFile.createWriter(conf, SequenceFile.Writer.file(path),
      SequenceFile.Writer.keyClass(keySchema.mf.erasure), SequenceFile.Writer.valueClass(valueSchema.mf.erasure))
    new ConvertingSequenceFileWriter[K, V](writer)
  }

  def fromLocal[K, V](uri: String)
                     (implicit keySchema: SeqSchema[K],
                      valueSchema: SeqSchema[V]): ConvertingSequenceFileWriter[K, V] =
    fromUri(new Configuration(), uri)
}