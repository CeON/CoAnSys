/*
 * This file is part of CoAnSys project.
 * Copyright (c) 20012-2013 ICM-UW
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
import org.apache.hadoop.io.{BytesWritable, Writable}
import org.apache.hadoop.mapreduce.Mapper
import pl.edu.icm.coansys.models.DocumentProtos.DocumentWrapper

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
