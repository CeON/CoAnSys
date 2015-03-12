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

import org.apache.hadoop.mapreduce.Reducer
import org.apache.hadoop.io.{BytesWritable, Text}
import pl.edu.icm.coansys.citations.data.{MarkedBytesWritable, MarkedText}

/**
 * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
 */
class AuthorJoinerStepOne extends Reducer[MarkedText, MarkedBytesWritable, MarkedText, BytesWritable] {
  type Context = Reducer[MarkedText, MarkedBytesWritable, MarkedText, BytesWritable]#Context

  val outMarkedKey = new MarkedText(marked = true)
  val outUnmarkedKey = new MarkedText(marked = false)

  override def reduce(key: MarkedText, values: java.lang.Iterable[MarkedBytesWritable], context: Context) {
    var leftCount = 0
    for (value <- values) {
      if (value.isMarked.get()) {
        outMarkedKey.text.set(key.text.toString + "_" + leftCount)
        leftCount += 1
        context.write(outMarkedKey, value.bytes)
      } else {
        for (i <- 0 until leftCount) {
          outUnmarkedKey.text.set(key.text.toString + "_" + i)
          context.write(outUnmarkedKey, value.bytes)
        }
      }
    }
  }
}
