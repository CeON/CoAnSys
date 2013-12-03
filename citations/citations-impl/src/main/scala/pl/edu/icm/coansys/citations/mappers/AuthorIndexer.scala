package pl.edu.icm.coansys.citations.mappers

import org.apache.hadoop.io.{Text, BytesWritable, Writable}
import org.apache.hadoop.mapreduce.Mapper
import pl.edu.icm.coansys.citations.data.{MarkedBytesWritable, MarkedText, MatchableEntity}
import pl.edu.icm.coansys.citations.util.misc
import pl.edu.icm.coansys.citations.util.misc._

/**
 * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
 */
class AuthorIndexer extends Mapper[Writable, BytesWritable, MarkedText, MarkedBytesWritable] {
  type Context = Mapper[Writable, BytesWritable, MarkedText, MarkedBytesWritable]#Context
  val outKey = new MarkedText()
  val outValue = new MarkedBytesWritable()

  override def map(key: Writable, value: BytesWritable, context: Context) {
    val entity = MatchableEntity.fromBytes(value.copyBytes())
    val authorTokens = misc.lettersNormaliseTokenise(entity.author).filterNot(stopWords).distinct
    authorTokens.foreach{ token =>
      outKey.text.set(token + entity.year)
      outValue.bytes.set(value)
      context.write(outKey, outValue)
    }
  }
}
