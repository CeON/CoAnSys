package pl.edu.icm.coansys.citations.reducers

import collection.JavaConversions._
import org.apache.hadoop.io.{BytesWritable, Text}
import org.apache.hadoop.mapreduce.Reducer
import pl.edu.icm.coansys.models.PICProtos.{Reference, PicOut}

/**
 * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
 */
class OutputConverter extends Reducer[Text, BytesWritable, Text, BytesWritable] {
  type Context = Reducer[Text, BytesWritable, Text, BytesWritable]#Context

  val outValue = new BytesWritable()

  override def reduce(key: Text, values: java.lang.Iterable[BytesWritable], context: Context) {
    val out = PicOut.newBuilder()
    out.setDocId(key.toString)
    for (value <- values) {
      out.addRefs(Reference.parseFrom(value.copyBytes()))
    }

    val bytes = out.build().toByteArray
    outValue.set(bytes, 0, bytes.length)
    context.write(key, outValue)
  }
}