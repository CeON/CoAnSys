package pl.edu.icm.coansys.citations.mappers

import collection.JavaConversions._
import org.apache.hadoop.io.{BytesWritable, Text, Writable}
import org.apache.hadoop.mapreduce.Mapper
import pl.edu.icm.cermine.bibref.CRFBibReferenceParser
import pl.edu.icm.coansys.citations.data.MatchableEntity
import pl.edu.icm.coansys.models.DocumentProtos.DocumentWrapper
import org.slf4j.LoggerFactory

/**
 * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
 */
class ReferenceExtractorAndParser extends Mapper[Writable, BytesWritable, Text, BytesWritable] {
  type Context = Mapper[Writable, BytesWritable, Text, BytesWritable]#Context
  val maxSupportedCitationLength = 2000
  private val logger = LoggerFactory.getLogger(classOf[ReferenceExtractorAndParser])
  private val keyWritable = new Text()
  private val valueWritable = new BytesWritable()
  private var parser: CRFBibReferenceParser = null

  override def setup(context: Context) {
    val model = context.getConfiguration.get("crf.reference.parser.model")
    parser =
      if(model != null)
        new CRFBibReferenceParser(model)
      else
        new CRFBibReferenceParser(
          this.getClass.getResourceAsStream("/pl/edu/icm/cermine/bibref/acrf-small.ser.gz"))
  }

  override def map(key: Writable, value: BytesWritable, context: Context) {
    val wrapper = DocumentWrapper.parseFrom(value.copyBytes())
    wrapper.getDocumentMetadata.getReferenceList.filterNot{_.getRawCitationText.isEmpty}.foreach {
      case ref =>
        if (ref.getRawCitationText.length > maxSupportedCitationLength) {
          logger.warn(s"Citation ${ref.getPosition} in document ${wrapper.getRowId} exceeds max supported citation length. Omitted.")
        } else {
          val entity = MatchableEntity.fromUnparsedReferenceMetadata(parser, ref)
          val bytes = entity.data.toByteArray
          keyWritable.set(entity.id)
          valueWritable.set(bytes, 0, bytes.length)
          context.write(keyWritable, valueWritable)
        }
    }
  }
}