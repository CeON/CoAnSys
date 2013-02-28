/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */

package pl.edu.icm.coansys.citations.jobs

import scala.collection.JavaConversions._
import org.apache.hadoop.mapreduce.{Job, Mapper}
import org.apache.hadoop.io.{Writable, Text, BytesWritable}
import pl.edu.icm.coansys.importers.models.DocumentProtos.DocumentWrapper
import pl.edu.icm.coansys.citations.data.{SimilarityMeasurer, Entity, CitationEntity}
import pl.edu.icm.coansys.citations.indices.{EntityIndex, AuthorIndex}
import org.apache.hadoop.conf.Configured
import org.apache.hadoop.util.{ToolRunner, Tool}
import org.apache.hadoop.fs.Path
import org.apache.hadoop.mapreduce.lib.input.{SequenceFileInputFormat, FileInputFormat}
import org.apache.hadoop.mapreduce.lib.output.{SequenceFileOutputFormat, FileOutputFormat}
import pl.edu.icm.cermine.bibref.{CRFBibReferenceParser, BibReferenceParser}
import pl.edu.icm.cermine.bibref.model.BibEntry

/**
 * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
 */
object MatcherLowLevel extends Configured with Tool {

  class CitationExtractor extends Mapper[Writable, BytesWritable, BytesWritable, BytesWritable] {
    type Context = Mapper[Writable, BytesWritable, BytesWritable, BytesWritable]#Context
    val writable = new BytesWritable()
    var parser: BibReferenceParser[BibEntry] = null

    override def setup(context: Context) {
      val parserModelUri = context.getConfiguration.get("bibref.parser.model")
      parser = new CRFBibReferenceParser(parserModelUri)
    }

    override def map(key: Writable, value: BytesWritable, context: Context) {
      val wrapper = DocumentWrapper.parseFrom(value.copyBytes())
      wrapper.getDocumentMetadata.getReferenceList.map(CitationEntity.fromUnparsedReferenceMetadata(parser, _)).foreach {
        case ent =>
          val bytes = ent.toTypedBytes
          writable.set(bytes, 0, bytes.length)
          context.write(writable, writable)
      }
    }
  }

  class HeuristicAdder extends Mapper[BytesWritable, BytesWritable, BytesWritable, Text] {
    type Context = Mapper[BytesWritable, BytesWritable, BytesWritable, Text]#Context
    val keyWritable = new BytesWritable()
    val valueWritable = new Text()
    val index: AuthorIndex = null

    override def map(key: BytesWritable, value: BytesWritable, context: Context) {
      val cit = Entity.fromTypedBytes(key.copyBytes()).asInstanceOf[CitationEntity]
      Matcher.approximatelyMatchingDocuments(cit, index).foreach {
        case (entityId) =>
          val bytes = cit.toTypedBytes
          keyWritable.set(bytes, 0, bytes.length)
          valueWritable.set(entityId)
          context.write(keyWritable, valueWritable)
      }
    }

    override def cleanup(context: Context) {
      index.close()
    }
  }

  class ExactMatcher extends Mapper[BytesWritable, Text, Text, Text] {
    type Context = Mapper[BytesWritable, Text, Text, Text]#Context
    val keyWritable = new Text()
    val valueWritable = new Text()
    val index: EntityIndex = null
    val similarityMeasurer = new SimilarityMeasurer

    override def map(key: BytesWritable, value: Text, context: Context) {
      val minimalSimilarity = 0.5
      val cit = Entity.fromTypedBytes(key.copyBytes())
      val entity = index.getEntityById(value.toString)
      val similarity = similarityMeasurer.similarity(cit, entity)
      if (similarity >= minimalSimilarity) {
        keyWritable.set(cit.entityId)
        valueWritable.set(entity.entityId)
        context.write(keyWritable, valueWritable)
      }
    }

    override def cleanup(context: Context) {
      index.close()
    }
  }

  def run(args: Array[String]) = {
    val parserModelUri = args(0)
    //    val keyIndexUri = args(1)
    //    val authorIndexUri = args(2)
    val documentsUri = args(1)
    val outUri = args(2)
    val conf = getConf
    conf.set("bibref.parser.model", parserModelUri)
    val extractionJob = new Job(conf, "References extractor")
    extractionJob.setJarByClass(getClass)

    FileInputFormat.addInputPath(extractionJob, new Path(documentsUri))
    FileOutputFormat.setOutputPath(extractionJob, new Path(outUri))

    extractionJob.setMapperClass(classOf[CitationExtractor])
    extractionJob.setNumReduceTasks(0)
    extractionJob.setOutputKeyClass(classOf[BytesWritable])
    extractionJob.setOutputValueClass(classOf[BytesWritable])
    extractionJob.setInputFormatClass(classOf[SequenceFileInputFormat[BytesWritable, BytesWritable]])
    extractionJob.setOutputFormatClass(classOf[SequenceFileOutputFormat[BytesWritable, BytesWritable]])

    if (extractionJob.waitForCompletion(true))
      0
    else
      1
  }

  def main(args: Array[String]) {
    val exitCode = ToolRunner.run(MatcherLowLevel, args)
    System.exit(exitCode)
  }
}
