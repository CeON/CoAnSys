package pl.edu.icm.coansys.citations.reducers

import collection.JavaConversions._
import org.apache.hadoop.io.BytesWritable
import org.apache.hadoop.mapreduce.Reducer
import pl.edu.icm.coansys.citations.data.{MarkedBytesWritable, MarkedText}


/**
 * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
 */
class AuthorJoinerStepTwo extends Reducer[MarkedText, BytesWritable, BytesWritable, BytesWritable] {
  type Context = Reducer[MarkedText, BytesWritable, BytesWritable, BytesWritable]#Context

  val left = new BytesWritable()

  override def reduce(key: MarkedText, values: java.lang.Iterable[BytesWritable], context: Context) {
    val it = values.iterator()
    left.set(it.next())
    for (value <- it) {
      context.write(left, value)
    }
  }
}