package pl.edu.icm.coansys.citations.mappers

import org.apache.hadoop.mapreduce.Mapper
import org.apache.hadoop.io.{Text, BytesWritable}
import pl.edu.icm.coansys.citations.data.{TextNumericWritable, MatchableEntity, BytesPairWritable}
import pl.edu.icm.coansys.citations.util.misc._

/**
 * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
 */
class PreAssesor extends Mapper[BytesWritable, BytesWritable, TextNumericWritable, BytesPairWritable] {
  type Context = Mapper[BytesWritable, BytesWritable, TextNumericWritable, BytesPairWritable]#Context
  val outKey = new TextNumericWritable()
  val outValue = new BytesPairWritable()

  override def map(key: BytesWritable, value: BytesWritable, context: Context) {
    val src = MatchableEntity.fromBytes(key.copyBytes())
    val dst = MatchableEntity.fromBytes(value.copyBytes())

    val srcTokens = niceTokens(src.toReferenceString)
    val dstTokens = niceTokens(dst.toReferenceString)

    if (srcTokens.size + dstTokens.size == 0) return

    val similarity = 2.0 * (srcTokens & dstTokens).size / (srcTokens.size + dstTokens.size)
    outKey.text.set(src.id)
    outKey.numeric.set(similarity)
    outValue.left.set(key)
    outValue.right.set(value)
    context.write(outKey, outValue)
  }
}