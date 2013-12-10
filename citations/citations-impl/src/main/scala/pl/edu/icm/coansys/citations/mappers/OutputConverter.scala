package pl.edu.icm.coansys.citations.mappers

import org.apache.hadoop.io.{Text, BytesWritable}
import org.apache.hadoop.mapreduce.Mapper
import pl.edu.icm.coansys.citations.data.entity_id.{DocEntityId, CitEntityId}
import pl.edu.icm.coansys.models.PICProtos.Reference

/**
 * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
 */
class OutputConverter  extends Mapper[Text, Text, Text, BytesWritable] {
  type Context = Mapper[Text, Text, Text, BytesWritable]#Context
  val keyWritable = new Text()
  val valueWritable = new BytesWritable()

  override def map(src: Text, dst: Text, context: Context) {
    val srcId = CitEntityId.fromString(src.toString)
    val dstId = DocEntityId.fromString(dst.toString.split(":", 2)(1))
    val ref = Reference.newBuilder().setRefNum(srcId.position).setDocId(dstId.documentId).build()
    val bytes = ref.toByteArray
    keyWritable.set(srcId.sourceDocumentId)
    valueWritable.set(bytes, 0, bytes.length)
    context.write(keyWritable, valueWritable)
  }
}