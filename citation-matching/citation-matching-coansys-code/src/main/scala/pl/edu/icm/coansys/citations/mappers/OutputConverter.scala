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