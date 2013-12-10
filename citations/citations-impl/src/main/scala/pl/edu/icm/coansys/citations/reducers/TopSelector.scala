package pl.edu.icm.coansys.citations.reducers

import collection.JavaConversions._
import org.apache.hadoop.mapreduce.Reducer
import org.apache.hadoop.io.BytesWritable
import pl.edu.icm.coansys.citations.data.{TextNumericWritable, BytesPairWritable}

/**
 * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
 */
class TopSelector extends Reducer[TextNumericWritable, BytesPairWritable, BytesWritable, BytesWritable] {
  type Context = Reducer[TextNumericWritable, BytesPairWritable, BytesWritable, BytesWritable]#Context
  val n = 100
  override def reduce(key: TextNumericWritable, values: java.lang.Iterable[BytesPairWritable], context: Context) {
    for(value <- values.take(n)) {
      context.write(value.left, value.right)
    }
  }
}