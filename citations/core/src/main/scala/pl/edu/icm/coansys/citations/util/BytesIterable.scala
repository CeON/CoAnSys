/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */

package pl.edu.icm.coansys.citations.util

import java.io.DataInput
import java.io.DataOutput
import org.apache.hadoop.io.Writable

class BytesIterable(var iterable: Iterable[Array[Byte]]) extends Writable {
  def this() = this(Nil)

  def write(out: DataOutput) {
    out.writeInt(iterable.size)
    iterable foreach {
      x =>
        out.writeInt(x.length)
        out.write(x, 0, x.length)
    }
  }

  def readFields(in: DataInput) {
    val size = in.readInt()
    iterable = (1 to size).map {
      _ =>
        val len = in.readInt()
        val buff = new Array[Byte](len)
        in.readFully(buff, 0, len)
        buff
    }
  }

  override def toString =
    iterable.map(new String(_, "UTF-8")).mkString("\n")
}