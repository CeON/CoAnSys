/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */

package pl.edu.icm.coansys.citations.mappers

import org.apache.hadoop.io.{Writable, Text, BytesWritable}
import org.apache.hadoop.mapreduce.Mapper
import pl.edu.icm.coansys.citations.data.MatchableEntity
import pl.edu.icm.coansys.citations.indices.AuthorIndex
import pl.edu.icm.coansys.citations.jobs.Matcher


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
    Matcher.approximatelyMatchingDocuments(entity, index).foreach {
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

