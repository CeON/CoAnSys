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

package pl.edu.icm.coansys.citations.reducers

import collection.JavaConversions._
import org.apache.hadoop.io.{BytesWritable, Text}
import org.apache.hadoop.mapreduce.Reducer
import pl.edu.icm.coansys.models.PICProtos.{Reference, PicOut}

/**
 * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
 */
class OutputConverter extends Reducer[Text, BytesWritable, Text, BytesWritable] {
  type Context = Reducer[Text, BytesWritable, Text, BytesWritable]#Context

  val outValue = new BytesWritable()

  override def reduce(key: Text, values: java.lang.Iterable[BytesWritable], context: Context) {
    val out = PicOut.newBuilder()
    out.setDocId(key.toString)
    for (value <- values) {
      out.addRefs(Reference.parseFrom(value.copyBytes()))
    }

    val bytes = out.build().toByteArray
    outValue.set(bytes, 0, bytes.length)
    context.write(key, outValue)
  }
}