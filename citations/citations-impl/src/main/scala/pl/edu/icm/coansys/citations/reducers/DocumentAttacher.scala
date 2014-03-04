/*
 * This file is part of CoAnSys project.
 * Copyright (c) 2012-2014 ICM-UW
 *
 * CoAnSys is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
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
import org.apache.hadoop.io.BytesWritable
import org.apache.hadoop.mapreduce.Reducer
import pl.edu.icm.coansys.citations.data.{TextWithBytesWritable, MarkedText}

/**
 * Created by matfed on 01.03.14.
 */
class DocumentAttacher  extends Reducer[MarkedText, TextWithBytesWritable, MarkedText, TextWithBytesWritable] {
  type Context = Reducer[MarkedText, TextWithBytesWritable, MarkedText, TextWithBytesWritable]#Context

  val outKey = new MarkedText(false)
  val outValue = new TextWithBytesWritable()

  override def reduce(key: MarkedText, values: java.lang.Iterable[TextWithBytesWritable], context: Context) {
    val iterator = values.iterator()
    val first = iterator.next()
    if (first.text.getLength == 0) {
      outValue.bytes.set(first.bytes)
    } else {
      return
    }
    for (id: TextWithBytesWritable <- iterator) {
      outKey.text.set(id.text.toString)
      context.write(outKey, outValue)
    }
  }
}
