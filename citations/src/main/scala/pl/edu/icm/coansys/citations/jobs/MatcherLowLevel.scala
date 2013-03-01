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
import org.apache.hadoop.fs.{FileSystem, Path}
import org.apache.hadoop.mapreduce.lib.input.{SequenceFileInputFormat, FileInputFormat}
import org.apache.hadoop.mapreduce.lib.output.{TextOutputFormat, SequenceFileOutputFormat, FileOutputFormat}
import pl.edu.icm.cermine.bibref.{CRFBibReferenceParser, BibReferenceParser}
import pl.edu.icm.cermine.bibref.model.BibEntry

/**
 * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
 */
object MatcherLowLevel extends Configured with Tool {

  class CitationExtractor extends Mapper[Writable, BytesWritable, BytesWritable, BytesWritable] {
    type Context = Mapper[Writable, BytesWritable, BytesWritable, BytesWritable]#Context
    val writable = new BytesWritable()
    val emptyWritable = new BytesWritable()
    var parser: BibReferenceParser[BibEntry] = null

    override def setup(context: Context) {
      val parserModelUri = context.getConfiguration.get("bibref.parser.model")
      parser = new CRFBibReferenceParser(parserModelUri)
    }

    override def map(key: Writable, value: BytesWritable, context: Context) {
      val wrapper = DocumentWrapper.parseFrom(value.copyBytes())
      wrapper.getDocumentMetadata.getReferenceList.filterNot(_.getRawCitationText.isEmpty)
        .map(CitationEntity.fromUnparsedReferenceMetadata(parser, _)).foreach {
        case ent =>
          val bytes = ent.toTypedBytes
          writable.set(bytes, 0, bytes.length)
          context.write(writable, emptyWritable)
      }
    }
  }

  class HeuristicAdder extends Mapper[BytesWritable, BytesWritable, BytesWritable, Text] {
    type Context = Mapper[BytesWritable, BytesWritable, BytesWritable, Text]#Context
    val keyWritable = new BytesWritable()
    val valueWritable = new Text()
    var index: AuthorIndex = null

    override def setup(context: Context) {
      val authorIndexUri = context.getConfiguration.get("index.author")
      index = new AuthorIndex(authorIndexUri)
    }

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
      if (index != null)
        index.close()
    }
  }

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
      val cit = Entity.fromTypedBytes(key.copyBytes())
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

  def run(args: Array[String]): Int = {
    val parserModelUri = args(0)
    val keyIndexUri = args(1)
    val authorIndexUri = args(2)
    val documentsUri = args(3)
    val outUri = args(4)
    val conf = getConf
    val extractedRefsUri = outUri + "_refs"
    val refsHeuristicUri = outUri + "_heur"
    conf.set("bibref.parser.model", parserModelUri)
    conf.set("index.key", keyIndexUri)
    conf.set("index.author", authorIndexUri)
    val fs = FileSystem.get(conf)

    val extractionJob = new Job(conf, "References extractor")
    extractionJob.setJarByClass(getClass)

    FileInputFormat.addInputPath(extractionJob, new Path(documentsUri))
    FileOutputFormat.setOutputPath(extractionJob, new Path(extractedRefsUri))

    extractionJob.setMapperClass(classOf[CitationExtractor])
    extractionJob.setNumReduceTasks(0)
    extractionJob.setOutputKeyClass(classOf[BytesWritable])
    extractionJob.setOutputValueClass(classOf[BytesWritable])
    extractionJob.setInputFormatClass(classOf[SequenceFileInputFormat[BytesWritable, BytesWritable]])
    extractionJob.setOutputFormatClass(classOf[SequenceFileOutputFormat[BytesWritable, BytesWritable]])

    if (!extractionJob.waitForCompletion(true))
      return 1

    val heuristicJob = new Job(conf, "Heuristic adder")
    heuristicJob.setJarByClass(getClass)

    FileInputFormat.addInputPath(heuristicJob, new Path(extractedRefsUri))
    FileOutputFormat.setOutputPath(heuristicJob, new Path(refsHeuristicUri))

    heuristicJob.setMapperClass(classOf[HeuristicAdder])
    heuristicJob.setNumReduceTasks(0)
    heuristicJob.setOutputKeyClass(classOf[BytesWritable])
    heuristicJob.setOutputValueClass(classOf[Text])
    heuristicJob.setInputFormatClass(classOf[SequenceFileInputFormat[BytesWritable, BytesWritable]])
    heuristicJob.setOutputFormatClass(classOf[SequenceFileOutputFormat[BytesWritable, Text]])

    fs.deleteOnExit(new Path(extractedRefsUri))

    if (!heuristicJob.waitForCompletion(true))
      return 1

    val assessorJob = new Job(conf, "Similarity assessor")
    assessorJob.setJarByClass(getClass)

    FileInputFormat.addInputPath(assessorJob, new Path(refsHeuristicUri))
    FileOutputFormat.setOutputPath(assessorJob, new Path(outUri))

    assessorJob.setMapperClass(classOf[ExactMatcher])
    assessorJob.setNumReduceTasks(0)
    assessorJob.setOutputKeyClass(classOf[BytesWritable])
    assessorJob.setOutputValueClass(classOf[Text])
    assessorJob.setInputFormatClass(classOf[SequenceFileInputFormat[BytesWritable, BytesWritable]])
    assessorJob.setOutputFormatClass(classOf[TextOutputFormat[String, String]])

    fs.deleteOnExit(new Path(refsHeuristicUri))

    if (!assessorJob.waitForCompletion(true))
      return 1
    else
      return 0
  }

  def main(args: Array[String]) {
    val exitCode = ToolRunner.run(MatcherLowLevel, args)
    System.exit(exitCode)
  }
}
