/*
 * This file is part of CoAnSys project.
 * Copyright (c) 2012-2015 ICM-UW
 * 
 * CoAnSys is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.

 * CoAnSys is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with CoAnSys. If not, see <http://www.gnu.org/licenses/>.
 */

package pl.edu.icm.coansys.citations.mappers

import collection.JavaConversions._
import org.apache.hadoop.io.{BytesWritable, Text, Writable}
import org.apache.hadoop.mapreduce.Mapper
import pl.edu.icm.cermine.bibref.CRFBibReferenceParser
import pl.edu.icm.coansys.citations.data.MatchableEntity
import pl.edu.icm.coansys.models.DocumentProtos.DocumentWrapper
import org.slf4j.LoggerFactory
import pl.edu.icm.coansys.citations.data.entity_id.CitEntityId
import org.hsqldb.lib.StringUtil
import org.apache.commons.lang.StringUtils
import scala.util.Try

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
      if (model != null)
        new CRFBibReferenceParser(model)
      else
        new CRFBibReferenceParser(
          this.getClass.getResourceAsStream("/pl/edu/icm/cermine/bibref/acrf.ser.gz"))
  }

  override def map(key: Writable, value: BytesWritable, context: Context) {
    val wrapper = DocumentWrapper.parseFrom(value.copyBytes())
    wrapper.getDocumentMetadata.getReferenceList.filterNot(s => StringUtils.isBlank(s.getRawCitationText)).foreach {
      case ref =>
        try {
          if (ref.getRawCitationText.length > maxSupportedCitationLength) {
            logger.warn(s"Citation ${ref.getPosition} in document ${wrapper.getRowId} exceeds max supported citation length. Omitted.")
          } else {
            val citId = new CitEntityId(wrapper.getDocumentMetadata.getKey, ref.getPosition)
            val entity = MatchableEntity.fromUnparsedReference(parser, citId.toString, ref.getRawCitationText)
            val bytes = entity.data.toByteArray
            keyWritable.set(entity.id)
            valueWritable.set(bytes, 0, bytes.length)
            context.write(keyWritable, valueWritable)
          }
        } catch {
          case e: Exception =>
            logger.error(s"Error while parsing citation ${ref.getPosition} in document ${wrapper.getRowId}", e)
        }
    }
  }
}
