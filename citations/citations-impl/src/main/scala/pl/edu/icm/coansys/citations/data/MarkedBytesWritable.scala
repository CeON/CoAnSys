package pl.edu.icm.coansys.citations.data

import java.io.{DataOutput, DataInput}
import org.apache.hadoop.io.{BooleanWritable, BytesWritable, Writable}

/**
 * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
 */
class MarkedBytesWritable(val marked: Boolean) extends Writable {
  def this() = this(false)

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
