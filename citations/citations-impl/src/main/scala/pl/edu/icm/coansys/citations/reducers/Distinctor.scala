package pl.edu.icm.coansys.citations.reducers

import org.apache.hadoop.mapreduce.Reducer
import pl.edu.icm.coansys.citations.data.{BytesPairWritable, MarkedBytesWritable, MarkedText}
import org.apache.hadoop.io.{Text, BytesWritable}

/**
 * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
 */
class Distinctor extends Reducer[Text, BytesPairWritable, BytesWritable, BytesWritable] {
  type Context = Reducer[Text, BytesPairWritable, BytesWritable, BytesWritable]#Context

  override def reduce(key: Text, values: java.lang.Iterable[BytesPairWritable], context: Context) {
    val first = values.iterator().next()
    context.write(first.left, first.right)
  }
}