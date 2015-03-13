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
class BytesPairWritable(val left: BytesWritable, val right: BytesWritable) extends Writable {
  def this() = this(new BytesWritable(), new BytesWritable())
  def this(left: Array[Byte], right: Array[Byte]) = this(new BytesWritable(left), new BytesWritable(right))

  def readFields(in: DataInput) {
    left.readFields(in)
    right.readFields(in)
  }

  def write(out: DataOutput) {
    left.write(out)
    right.write(out)
  }

  override def hashCode() = left.hashCode() + 31 * right.hashCode()

  override def equals(obj: scala.Any) = obj match {
    case o: BytesPairWritable =>
      left.equals(o.left) && right.equals(o.right)
    case _ => false
  }
}
