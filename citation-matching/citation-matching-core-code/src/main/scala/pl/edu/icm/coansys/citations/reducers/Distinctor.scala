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

import org.apache.hadoop.mapreduce.Reducer
import pl.edu.icm.coansys.citations.data.{BytesPairWritable, MarkedBytesWritable, MarkedText}
import org.apache.hadoop.io.{Text, BytesWritable}
import pl.edu.icm.coansys.citations.indices.AuthorIndex

/**
 * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
 */
class Distinctor extends Reducer[Text, BytesPairWritable, BytesWritable, BytesWritable] {
  type Context = Reducer[Text, BytesPairWritable, BytesWritable, BytesWritable]#Context
  var minOccurrences = 1

  override def setup(context: Context) {
    minOccurrences = context.getConfiguration.getInt("distinctor.min.occurrences", 1)
  }

  override def reduce(key: Text, values: java.lang.Iterable[BytesPairWritable], context: Context) {
    val iterator = values.iterator()
    val first = iterator.next()
    var left = minOccurrences - 1
    while (left > 0 && iterator.hasNext) {
      iterator.next()
      left -= 1
    }
    if (left == 0)
      context.write(first.left, first.right)
  }
}