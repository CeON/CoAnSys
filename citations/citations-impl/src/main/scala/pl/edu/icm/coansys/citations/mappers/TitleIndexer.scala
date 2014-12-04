package pl.edu.icm.coansys.citations.mappers

import org.apache.hadoop.io.{BytesWritable, Writable}
import org.apache.hadoop.mapreduce.Mapper
import pl.edu.icm.coansys.citations.data.{MarkedBytesWritable, MarkedText, MatchableEntity}
import pl.edu.icm.coansys.citations.util.misc
import pl.edu.icm.coansys.citations.util.misc._

/**
 * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
 */
class TitleIndexer extends Mapper[Writable, BytesWritable, MarkedText, MarkedBytesWritable] {
  type Context = Mapper[Writable, BytesWritable, MarkedText, MarkedBytesWritable]#Context
  val outKey = new MarkedText()
  val outValue = new MarkedBytesWritable()
  val indexedTitleTokens = 4

  override def map(key: Writable, value: BytesWritable, context: Context) {
    val entity = MatchableEntity.fromBytes(value.copyBytes())
    val titleTokens = misc.lettersNormaliseTokenise(entity.title).filterNot(stopWords).take(indexedTitleTokens).distinct
    outValue.bytes.set(value)
    titleTokens.foreach{ token =>
      outKey.text.set(token + entity.year)
      context.write(outKey, outValue)
    }
  }
}
