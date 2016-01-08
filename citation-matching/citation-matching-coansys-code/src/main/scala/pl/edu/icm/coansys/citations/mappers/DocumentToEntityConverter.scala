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

import org.apache.hadoop.io.{Text, BytesWritable, Writable}
import org.apache.hadoop.mapreduce.Mapper
import pl.edu.icm.coansys.models.DocumentProtos.DocumentWrapper
import pl.edu.icm.coansys.citations.data.MatchableEntity
import pl.edu.icm.coansys.citations.converters.DocumentMetadataToEntityConverter

/**
 * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
 */
class DocumentToEntityConverter extends Mapper[Writable, BytesWritable, Text, BytesWritable] {
  type Context = Mapper[Writable, BytesWritable, Text, BytesWritable]#Context
  
  val documentToMatchableEntityConverter = new DocumentMetadataToEntityConverter()
  
  val keyWritable = new Text()
  val valueWritable = new BytesWritable()

  override def map(ignore: Writable, documentWritable: BytesWritable, context: Context) {
    val doc = DocumentWrapper.parseFrom(documentWritable.copyBytes())
    val entity = documentToMatchableEntityConverter.convert(doc.getDocumentMetadata)
    keyWritable.set(entity.id)
    val data = entity.data.toByteArray
    valueWritable.set(data, 0, data.length)

    context.write(keyWritable, valueWritable)
  }
}
