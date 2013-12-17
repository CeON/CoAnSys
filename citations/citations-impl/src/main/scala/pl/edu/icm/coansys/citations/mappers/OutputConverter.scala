package pl.edu.icm.coansys.citations.mappers

import org.apache.hadoop.io.{Text, BytesWritable}
import org.apache.hadoop.mapreduce.Mapper
import pl.edu.icm.coansys.citations.data.entity_id.{DocEntityId, CitEntityId}
import pl.edu.icm.coansys.models.PICProtos.Reference
import pl.edu.icm.coansys.citations.data.{MatchableEntity, TextWithBytesWritable}

/**
 * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
 */
class OutputConverter  extends Mapper[TextWithBytesWritable, Text, Text, BytesWritable] {
  type Context = Mapper[TextWithBytesWritable, Text, Text, BytesWritable]#Context
  val keyWritable = new Text()
  val valueWritable = new BytesWritable()

  override def map(src: TextWithBytesWritable, dst: Text, context: Context) {
    val srcId = CitEntityId.fromString(src.text.toString)
    val dstId = DocEntityId.fromString(dst.toString.split(":", 2)(1))
    val cit = MatchableEntity.fromBytes(src.bytes.copyBytes())

    val ref = Reference.newBuilder()
    ref.setRefNum(srcId.position)
    ref.setDocId(dstId.documentId)
    if (cit.rawText.isDefined)
      ref.setRawText(cit.rawText.get)

    val bytes = ref.build().toByteArray
    keyWritable.set(srcId.sourceDocumentId)
    valueWritable.set(bytes, 0, bytes.length)
    context.write(keyWritable, valueWritable)
  }
}