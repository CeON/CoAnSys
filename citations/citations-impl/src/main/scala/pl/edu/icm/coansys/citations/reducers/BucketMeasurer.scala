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

import org.apache.hadoop.io.{NullWritable, Text}
import org.apache.hadoop.mapreduce.Reducer
import pl.edu.icm.coansys.citations.data.MarkedText

import scala.collection.JavaConversions._
import scala.collection.mutable.ListBuffer


/**
 * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
 */
class BucketMeasurer extends Reducer[MarkedText, MarkedText, Text, NullWritable] {
  type Context = Reducer[MarkedText, MarkedText, Text, NullWritable]#Context

  val outValue = new Text()

  var maxSize: Long = 0

  override def setup(context: Context) {
    maxSize = context.getConfiguration.getLong("max.bucket.size", 0)
  }
  
  override def reduce(key: MarkedText, values: java.lang.Iterable[MarkedText], context: Context) {
    var docCount: Long = 0
    var citCount: Long = 0
    for (value <- values) {
      if (value.isMarked.get()) {
        docCount += 1
      } else {
        citCount += 1
      }
    }

    if (docCount * citCount <= maxSize || maxSize <= 0) {
      context.write(key.text, NullWritable.get())
    }
  }
}
