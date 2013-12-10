package pl.edu.icm.coansys.citations.mappers

import org.apache.hadoop.io.{Text, BytesWritable, Writable}
import org.apache.hadoop.mapreduce.Mapper
import pl.edu.icm.coansys.models.DocumentProtos.DocumentWrapper
import pl.edu.icm.coansys.citations.data.MatchableEntity

/**
 * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
 */
class DocumentToEntityConverter extends Mapper[Writable, BytesWritable, Text, BytesWritable] {
  type Context = Mapper[Writable, BytesWritable, Text, BytesWritable]#Context
  val keyWritable = new Text()
  val valueWritable = new BytesWritable()

  override def map(ignore: Writable, documentWritable: BytesWritable, context: Context) {
    val doc = DocumentWrapper.parseFrom(documentWritable.copyBytes())
    val entity = MatchableEntity.fromDocumentMetadata(doc.getDocumentMetadata)
    keyWritable.set(entity.id)
    val data = entity.data.toByteArray
    valueWritable.set(data, 0, data.length)

    context.write(keyWritable, valueWritable)
  }
}
