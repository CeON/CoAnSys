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

import org.apache.hadoop.mapreduce.Mapper
import org.apache.hadoop.io.{Writable, Text, BytesWritable}
import pl.edu.icm.coansys.citations.data.{MatchableEntity, BytesPairWritable}

/**
 * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
 */
class LeftEntityExtractor extends Mapper[BytesWritable, Writable, Text, BytesWritable] {
  type Context = Mapper[BytesWritable, Writable, Text, BytesWritable]#Context
  val id = new Text()

  override def map(entityBytes: BytesWritable, ignore: Writable, context: Context) {
    val entity = MatchableEntity.fromBytes(entityBytes.copyBytes())
    id.set(entity.id)
    context.write(id, entityBytes)
  }
}