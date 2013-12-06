package pl.edu.icm.coansys.citations.data

import java.io.{DataOutput, DataInput}
import org.apache.hadoop.io.{BooleanWritable, BytesWritable, Writable}

/**
 * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
 */
class BytesPairWritable extends Writable {

  val left = new BytesWritable()
  val right = new BytesWritable()

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
