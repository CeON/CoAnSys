package pl.edu.icm.coansys.citations.reducers

import collection.JavaConversions._

import org.apache.hadoop.mapreduce.Reducer
import org.apache.hadoop.io.{BytesWritable, Text}
import pl.edu.icm.coansys.citations.data.{MarkedBytesWritable, MarkedText}

/**
 * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
 */
class AuthorJoinerStepOne extends Reducer[MarkedText, MarkedBytesWritable, MarkedText, BytesWritable] {
  type Context = Reducer[MarkedText, MarkedBytesWritable, MarkedText, BytesWritable]#Context

  val outMarkedKey = new MarkedText(marked = true)
  val outUnmarkedKey = new MarkedText(marked = false)

  override def reduce(key: MarkedText, values: java.lang.Iterable[MarkedBytesWritable], context: Context) {
    var leftCount = 0
    for (value <- values) {
      if (value.isMarked.get()) {
        outMarkedKey.text.set(key.text.toString + "_" + leftCount)
        leftCount += 1
        context.write(outMarkedKey, value.bytes)
      } else {
        for (i <- 0 until leftCount) {
          outUnmarkedKey.text.set(key.text.toString + "_" + i)
          context.write(outUnmarkedKey, value.bytes)
        }
      }
    }
  }
}
