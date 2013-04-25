/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */

package pl.edu.icm.coansys.citations.mappers

import org.apache.hadoop.mapreduce.Mapper
import org.apache.hadoop.io.{Text, BytesWritable}
import pl.edu.icm.coansys.citations.data.{MatchableEntity, SimilarityMeasurer}
import pl.edu.icm.coansys.citations.indices.EntityIndex

/**
 * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
 */
class ExactMatcher extends Mapper[BytesWritable, Text, String, String] {
  type Context = Mapper[BytesWritable, Text, String, String]#Context
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
      context.write(cit.toReferenceString, entity.toReferenceString)
    }
  }

  override def cleanup(context: Context) {
    if (index != null)
      index.close()
  }
}
