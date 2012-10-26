package pl.edu.icm.coansys.citations

import java.io.DataInput
import java.io.DataOutput
import org.apache.hadoop.io.Writable
import com.nicta.scoobi.io.sequence.SeqSchema

/**
 * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
 */
class WritableIterable[T <: Writable : Manifest](var iterable: Iterable[T], factory: => T) extends Writable {
  def this() = this(Nil, manifest[T].erasure.newInstance().asInstanceOf[T])

  def write(out: DataOutput) {
    out.writeInt(iterable.size)
    //println("Writing size " + iterable.size)
    iterable foreach {
      x => x.write(out)
    }
  }

  def readFields(in: DataInput) {
    val size = in.readInt()
    //println("Read size " + size)
    iterable = (1 to size) map (_ => factory)
    iterable foreach {
      x => x.readFields(in)
    }
  }
}

/**
 * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
 */
class MockDocumentWritableIterable(var iter:Iterable[MockDocumentWrapper]) extends WritableIterable[MockDocumentWrapper](iter, new MockDocumentWrapper()) {
  def this() = this(Nil)
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