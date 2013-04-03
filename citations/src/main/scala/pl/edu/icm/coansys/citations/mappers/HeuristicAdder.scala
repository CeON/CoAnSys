/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */

package pl.edu.icm.coansys.citations.mappers

import org.apache.hadoop.io.{Text, BytesWritable}
import org.apache.hadoop.mapreduce.Mapper
import pl.edu.icm.cermine.bibref.{CRFBibReferenceParser, BibReferenceParser}
import pl.edu.icm.cermine.bibref.model.BibEntry
import pl.edu.icm.coansys.citations.data.MatchableEntity
import pl.edu.icm.coansys.citations.indices.AuthorIndex
import pl.edu.icm.coansys.citations.jobs.Matcher
import pl.edu.icm.coansys.importers.models.DocumentProtos.ReferenceMetadata


class HeuristicAdder extends Mapper[BytesWritable, BytesWritable, BytesWritable, Text] {
  type Context = Mapper[BytesWritable, BytesWritable, BytesWritable, Text]#Context
  val keyWritable = new BytesWritable()
  val valueWritable = new Text()
  var parser: BibReferenceParser[BibEntry] = null
  var index: AuthorIndex = null

  override def setup(context: Context) {
    val parserModelUri = context.getConfiguration.get("bibref.parser.model", "")
    if (!parserModelUri.isEmpty)
      parser = new CRFBibReferenceParser(parserModelUri)

    val authorIndexUri = context.getConfiguration.get("index.author")
    index = new AuthorIndex(authorIndexUri)
  }

  override def map(key: BytesWritable, value: BytesWritable, context: Context) {
    val meta = ReferenceMetadata.parseFrom(key.copyBytes())
    val cit =
      if (parser != null) {
        MatchableEntity.fromUnparsedReferenceMetadata(parser, meta)
      } else {
        MatchableEntity.fromReferenceMetadata(meta)
      }
    val bytes = cit.data.toByteArray
    keyWritable.set(bytes, 0, bytes.length)
    Matcher.approximatelyMatchingDocuments(cit, index).foreach {
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

