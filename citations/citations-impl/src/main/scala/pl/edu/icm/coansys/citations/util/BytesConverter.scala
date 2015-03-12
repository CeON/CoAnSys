/*
 * This file is part of CoAnSys project.
 * Copyright (c) 2012-2015 ICM-UW
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

package pl.edu.icm.coansys.citations.util

import com.nicta.scoobi.core.WireFormat
import com.nicta.scoobi.io.sequence.SeqSchema
import java.io.{DataInput, DataOutput}
import org.apache.hadoop.io.BytesWritable

/**
 * Class implementing Scoobi WireFormat and SeqSchema given serializer and deserializer functions.
 *
 * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
 */
class BytesConverter[T](serializer: T => Array[Byte], deserializer: Array[Byte] => T) extends WireFormat[T] with SeqSchema[T] {
  def toWire(x: T, out: DataOutput) {
    val bytes = serializer(x)
    out.writeInt(bytes.length)
    out.write(bytes, 0, bytes.length)
  }

  def fromWire(in: DataInput): T = {
    val size = in.readInt()
    val bytes = new Array[Byte](size)
    in.readFully(bytes, 0, size)
    deserializer(bytes)
  }

  type SeqType = BytesWritable

  def toWritable(x: T) = new BytesWritable(serializer(x))

  def fromWritable(x: BytesWritable) = deserializer(x.copyBytes())

  val mf = manifest[BytesWritable]
}