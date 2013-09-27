/*
 * This file is part of CoAnSys project.
 * Copyright (c) 2012-2013 ICM-UW
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

import org.apache.hadoop.io.{Writable, Text, BytesWritable}
import org.apache.hadoop.mapreduce.Mapper
import pl.edu.icm.coansys.citations.data.MatchableEntity
import pl.edu.icm.coansys.citations.indices.AuthorIndex
import pl.edu.icm.coansys.citations.util.matching


class HeuristicAdder extends Mapper[Writable, BytesWritable, BytesWritable, Text] {
  type Context = Mapper[Writable, BytesWritable, BytesWritable, Text]#Context
  val keyWritable = new BytesWritable()
  val valueWritable = new Text()
  var index: AuthorIndex = null

  override def setup(context: Context) {
    val authorIndexUri = context.getConfiguration.get("index.author")
    index = new AuthorIndex(authorIndexUri)
  }

  override def map(key: Writable, value: BytesWritable, context: Context) {
    val entity = MatchableEntity.fromBytes(value.copyBytes())
    keyWritable.set(value)
    matching.approximatelyMatchingDocuments(entity, index).foreach {
      case (entityId) =>
        valueWritable.set(entityId)
        context.write(keyWritable, valueWritable)
    }
  }

  override def cleanup(context: Context) {
    if (index != null)
      index.close()
  }
}

