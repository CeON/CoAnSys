package pl.edu.icm.coansys.citations.mappers

import org.apache.hadoop.io.{Text, BytesWritable}
import org.apache.hadoop.mapreduce.Mapper
import pl.edu.icm.coansys.citations.data.{BytesPairWritable, MatchableEntity}

/**
 * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
 */
class IdExtractor extends Mapper[BytesWritable, BytesWritable, Text, BytesPairWritable] {
  type Context = Mapper[BytesWritable, BytesWritable, Text, BytesPairWritable]#Context
  val outKey = new Text()
  val outValue = new BytesPairWritable()

  override def map(key: BytesWritable, value: BytesWritable, context: Context) {
    val left = MatchableEntity.fromBytes(key.copyBytes())
    val right = MatchableEntity.fromBytes(value.copyBytes())
    outKey.set(left.id + "#" + right.id)
    outValue.left.set(key)
    outValue.right.set(value)
    context.write(outKey, outValue)
  }
}