package pl.edu.icm.coansys.citations

import java.io.DataInput
import java.io.DataOutput
import org.apache.hadoop.io.Writable
import com.nicta.scoobi.io.sequence.SeqSchema

/**
 * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
 */
class WritableIterable[T <: Writable](var iterable: Iterable[T], factory: => T) extends Writable {
  def write(out: DataOutput) {
    out.writeInt(iterable.size)
    iterable foreach {
      x => x.write(out)
    }
  }

  def readFields(in: DataInput) {
    val size = in.readInt()
    iterable = (1 to size) map (_ => factory)
    iterable foreach {
      x => x.readFields(in)
    }
  }
}

/**
 * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
 */
class IterableSchema[T <: Writable : Manifest](factory: => T) extends SeqSchema[Iterable[T]] {

  def toWritable(x: Iterable[T]) = new WritableIterable[T](x, factory)

  def fromWritable(x: SeqType) =
    x.iterable

  type SeqType = WritableIterable[T]
  val mf = manifest[WritableIterable[T]]
}