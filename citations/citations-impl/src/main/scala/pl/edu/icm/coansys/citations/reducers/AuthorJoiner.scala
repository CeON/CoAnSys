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
    for (value <- values) {
      if (value.isMarked.get()) {
        left.set(value.bytes)
      } else {
        context.write(left, value.bytes)
      }
    }
  }

}