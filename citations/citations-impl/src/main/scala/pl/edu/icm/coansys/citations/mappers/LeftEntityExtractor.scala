package pl.edu.icm.coansys.citations.mappers

import org.apache.hadoop.mapreduce.Mapper
import org.apache.hadoop.io.{Writable, Text, BytesWritable}
import pl.edu.icm.coansys.citations.data.{MatchableEntity, BytesPairWritable}

/**
 * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
 */
class LeftEntityExtractor extends Mapper[BytesWritable, Writable, Text, BytesWritable] {
  type Context = Mapper[BytesWritable, BytesWritable, Text, BytesWritable]#Context
  val id = new Text()

  override def map(entityBytes: BytesWritable, ignore: Writable, context: Context) {
    val entity = MatchableEntity.fromBytes(entityBytes.copyBytes())
    id.set(entity.id)
    context.write(id, entityBytes)
  }
}