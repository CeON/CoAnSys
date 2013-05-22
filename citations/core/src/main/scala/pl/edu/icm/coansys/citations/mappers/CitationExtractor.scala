/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */

package pl.edu.icm.coansys.citations.mappers

import collection.JavaConversions._
import org.apache.hadoop.io.{BytesWritable, Writable}
import org.apache.hadoop.mapreduce.Mapper
import pl.edu.icm.coansys.importers.models.DocumentProtos.DocumentWrapper

/**
 * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
 */
class CitationExtractor extends Mapper[Writable, BytesWritable, BytesWritable, BytesWritable] {
  type Context = Mapper[Writable, BytesWritable, BytesWritable, BytesWritable]#Context
  val writable = new BytesWritable()
  val emptyWritable = new BytesWritable()

  override def map(key: Writable, value: BytesWritable, context: Context) {
    val wrapper = DocumentWrapper.parseFrom(value.copyBytes())
    wrapper.getDocumentMetadata.getReferenceList.filterNot(_.getRawCitationText.isEmpty).foreach {
      case ref =>
        val bytes = ref.toByteArray
        writable.set(bytes, 0, bytes.length)
        context.write(writable, emptyWritable)

    }
  }
}
