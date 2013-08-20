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

import org.apache.hadoop.mapreduce.Mapper
import org.apache.hadoop.io.{Text, BytesWritable}
import pl.edu.icm.coansys.citations.data.{MatchableEntity, SimilarityMeasurer}
import pl.edu.icm.coansys.citations.indices.EntityIndex

/**
 * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
 */
class ExactAssesor extends Mapper[BytesWritable, Text, Text, Text] {
  type Context = Mapper[BytesWritable, Text, Text, Text]#Context
  val keyWritable: Text = new Text()
  val valueWritable: Text = new Text()
  val similarityMeasurer = new SimilarityMeasurer
  var index: EntityIndex = null

  override def setup(context: Context) {
    val keyIndexUri = context.getConfiguration.get("index.key")
    index = new EntityIndex(keyIndexUri)
  }

  override def map(key: BytesWritable, value: Text, context: Context) {
    val minimalSimilarity = 0.5
    val cit = MatchableEntity.fromBytes(key.copyBytes())
    val entity = index.getEntityById(value.toString)
    val similarity = similarityMeasurer.similarity(cit, entity)
    if (similarity >= minimalSimilarity) {
      keyWritable.set(cit.id)
      valueWritable.set(similarity + ":" + entity.id)
      context.write(keyWritable, valueWritable)
    }
  }

  override def cleanup(context: Context) {
    if (index != null)
      index.close()
  }
}
