package pl.edu.icm.coansys.citations

import com.nicta.scoobi.core.WireFormat
import com.nicta.scoobi.io.sequence.SeqSchema
import java.io.{DataInput, DataOutput}
import org.apache.hadoop.io.BytesWritable

/**
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
    deserializer(bytes)
  }

  type SeqType = BytesWritable

  def toWritable(x: T) = new BytesWritable(serializer(x))

  def fromWritable(x: BytesWritable) = deserializer(x.getBytes)

  val mf = manifest[BytesWritable]
}