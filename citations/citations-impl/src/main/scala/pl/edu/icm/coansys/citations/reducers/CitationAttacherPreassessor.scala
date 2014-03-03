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
import pl.edu.icm.coansys.citations.data.{MatchableEntity, MarkedText}

/**
 * Created by matfed on 01.03.14.
 */
class CitationAttacherPreassessor  extends Reducer[MarkedText, BytesWritable, BytesWritable, BytesWritable] {
  type Context = Reducer[MarkedText, BytesWritable, BytesWritable, BytesWritable]#Context

  val outKey = new BytesWritable()
  val outValue = new BytesWritable()

  override def reduce(key: MarkedText, values: java.lang.Iterable[BytesWritable], context: Context) {
    val iterator = values.iterator()
    val first = iterator.next()
    val citation = MatchableEntity.fromBytes(first.copyBytes())
    outKey.set(first)

    for (value: BytesWritable <- iterator) {
      val document = MatchableEntity.fromBytes(value.copyBytes())
      context.write(outKey, value)

    }


  }
}
