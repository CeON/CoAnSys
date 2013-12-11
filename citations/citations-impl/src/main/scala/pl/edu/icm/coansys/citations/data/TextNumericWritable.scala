package pl.edu.icm.coansys.citations.data

import org.apache.hadoop.io.{WritableComparator, DoubleWritable, Text, WritableComparable}
import java.io.{DataOutput, DataInput}
import org.apache.hadoop.mapreduce.Partitioner

/**
 * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
 */
class TextNumericWritable(val text: Text, val numeric: DoubleWritable) extends WritableComparable[TextNumericWritable] {
  def this() = this(new Text(), new DoubleWritable())
  def this(text: String, numeric: Double) = this(new Text(text), new DoubleWritable(numeric))

  def readFields(in: DataInput) {
    text.readFields(in)
    numeric.readFields(in)
  }

  def write(out: DataOutput) {
    text.write(out)
    numeric.write(out)
  }

  def compareTo(o: TextNumericWritable): Int = {
    val textCompare = text.compareTo(o.text)
    if (textCompare == 0)
      -numeric.compareTo(o.numeric)
    else
      textCompare
  }

  override def hashCode(): Int = text.hashCode() + 31 * numeric.hashCode()

  override def equals(obj: scala.Any): Boolean = obj match {
    case o: TextNumericWritable =>
      text.equals(o.text) && numeric.equals(o.numeric)
    case _ => false
  }

  override def toString: String = text.toString + " " + numeric.toString
}

class TextNumericWritablePartitioner extends Partitioner[TextNumericWritable, Any] {
  def getPartition(key: TextNumericWritable, value: Any, partitions: Int) =
    (key.text.hashCode() & Integer.MAX_VALUE) % partitions
}

class TextNumericWritableSortComparator extends WritableComparator(classOf[TextNumericWritable], true) {
  override def compare(w1: WritableComparable[_], w2: WritableComparable[_]): Int = {
    val m1 = w1.asInstanceOf[TextNumericWritable]
    val m2 = w2.asInstanceOf[TextNumericWritable]

    m1.compareTo(m2)
  }
}

class TextNumericWritableGroupComparator extends WritableComparator(classOf[TextNumericWritable], true) {
  override def compare(w1: WritableComparable[_], w2: WritableComparable[_]): Int = {
    val m1 = w1.asInstanceOf[TextNumericWritable]
    val m2 = w2.asInstanceOf[TextNumericWritable]

    m1.text.compareTo(m2.text)
  }
}

