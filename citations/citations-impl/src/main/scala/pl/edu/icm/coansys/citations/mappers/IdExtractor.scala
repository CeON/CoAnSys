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
import pl.edu.icm.coansys.citations.data.{BytesPairWritable, MatchableEntity}

/**
 * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
 */
class IdExtractor extends Mapper[BytesWritable, BytesWritable, Text, BytesPairWritable] {
  type Context = Mapper[BytesWritable, BytesWritable, Text, BytesPairWritable]#Context
  val outKey = new Text()
  val outValue = new BytesPairWritable()

  override def map(key: BytesWritable, value: BytesWritable, context: Context) {
    val left = MatchableEntity.fromBytes(key.copyBytes())
    val right = MatchableEntity.fromBytes(value.copyBytes())
    outKey.set(left.id + "#" + right.id)
    outValue.left.set(key)
    outValue.right.set(value)
    context.write(outKey, outValue)
  }
}