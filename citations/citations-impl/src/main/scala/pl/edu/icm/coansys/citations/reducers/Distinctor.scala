package pl.edu.icm.coansys.citations.reducers

import org.apache.hadoop.mapreduce.Reducer
import pl.edu.icm.coansys.citations.data.{BytesPairWritable, MarkedBytesWritable, MarkedText}
import org.apache.hadoop.io.{Text, BytesWritable}
import pl.edu.icm.coansys.citations.indices.AuthorIndex

/**
 * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
 */
class Distinctor extends Reducer[Text, BytesPairWritable, BytesWritable, BytesWritable] {
  type Context = Reducer[Text, BytesPairWritable, BytesWritable, BytesWritable]#Context
  var minOccurrences = 1

  override def setup(context: Context) {
    minOccurrences = context.getConfiguration.getInt("distinctor.min.occurrences", 1)
  }

  override def reduce(key: Text, values: java.lang.Iterable[BytesPairWritable], context: Context) {
    val iterator = values.iterator()
    val first = iterator.next()
    var left = minOccurrences - 1
    while (left > 0 && iterator.hasNext) {
      iterator.next()
      left -= 1
    }
    if (left == 0)
      context.write(first.left, first.right)
  }
}