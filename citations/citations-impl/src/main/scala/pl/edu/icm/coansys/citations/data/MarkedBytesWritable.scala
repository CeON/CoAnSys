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

package pl.edu.icm.coansys.citations.data

import java.io.{DataOutput, DataInput}
import org.apache.hadoop.io.{BooleanWritable, BytesWritable, Writable}

/**
 * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
 */
class MarkedBytesWritable(val marked: Boolean) extends Writable {
  def this() = this(false)
  def this(bytes: Array[Byte], marked: Boolean = false) = {
    this(marked)
    this.bytes.set(bytes, 0, bytes.length)
  }

  val bytes = new BytesWritable()
  val isMarked = new BooleanWritable(marked)

  def readFields(in: DataInput) {
    bytes.readFields(in)
    isMarked.readFields(in)
  }

  def write(out: DataOutput) {
    bytes.write(out)
    isMarked.write(out)
  }

  override def hashCode() = bytes.hashCode() + 31 * isMarked.hashCode()

  override def equals(obj: scala.Any) = obj match {
    case o: MarkedBytesWritable =>
      bytes.equals(o.bytes) && isMarked.equals(o.isMarked)
    case _ => false
  }
}
