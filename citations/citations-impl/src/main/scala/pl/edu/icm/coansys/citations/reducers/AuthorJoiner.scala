package pl.edu.icm.coansys.citations.reducers

import collection.JavaConversions._

import org.apache.hadoop.mapreduce.Reducer
import org.apache.hadoop.io.{BytesWritable, Text}
import pl.edu.icm.coansys.citations.data.{MarkedBytesWritable, MarkedText}

/**
 * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
 */
class AuthorJoiner extends Reducer[MarkedText, MarkedBytesWritable, BytesWritable, BytesWritable] {
  type Context = Reducer[MarkedText, MarkedBytesWritable, BytesWritable, BytesWritable]#Context

  val left = new BytesWritable()

  override def reduce(key: MarkedText, values: java.lang.Iterable[MarkedBytesWritable], context: Context) {
    val it = values.iterator()
    val first = it.next()
    if (first.isMarked.get()) {
      left.set(first.bytes)
      for (value <- it) {
        context.write(left, value.bytes)
      }
    }
  }

}