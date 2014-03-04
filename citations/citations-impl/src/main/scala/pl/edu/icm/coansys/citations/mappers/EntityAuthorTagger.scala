package pl.edu.icm.coansys.citations.mappers

import org.apache.hadoop.mapreduce.Mapper
import org.apache.hadoop.io.{Text, BytesWritable, Writable}
import pl.edu.icm.coansys.citations.data.{MarkedBytesWritable, MarkedText, MatchableEntity}
import pl.edu.icm.coansys.citations.util.misc._

/**
 * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
 */
class EntityAuthorTagger extends Mapper[Writable, BytesWritable, MarkedText, MarkedBytesWritable] {
  type Context = Mapper[Writable, BytesWritable, MarkedText, MarkedBytesWritable]#Context
  val outKey = new MarkedText(marked = true)
  val outValue = new MarkedBytesWritable(marked = true)

  override def map(key: Writable, value: BytesWritable, context: Context) {
    val entity = MatchableEntity.fromBytes(value.copyBytes())
    val text = entity.rawText.getOrElse(List(entity.author, entity.year, entity.title).mkString(" "))
    val keys = for {
      year <- digitsNormaliseTokenise(text).filter(_.length == 4)
      approxYear <- approximateYear(year)
      author <- lettersNormaliseTokenise(text).distinct
    } yield author + approxYear

    outValue.bytes.set(value)
    keys.foreach {k =>
      outKey.text.set(k)
      context.write(outKey, outValue)
    }
  }
}