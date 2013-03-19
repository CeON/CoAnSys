/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */

package pl.edu.icm.coansys.citations.jobs

import org.apache.hadoop.mapreduce.Job
import org.apache.hadoop.io.{Text, BytesWritable}
import org.apache.hadoop.conf.{Configuration, Configured}
import org.apache.hadoop.util.{ToolRunner, Tool}
import org.apache.hadoop.fs.{FileSystem, Path}
import org.apache.hadoop.mapreduce.lib.input.{SequenceFileInputFormat, FileInputFormat}
import org.apache.hadoop.mapreduce.lib.output.{SequenceFileOutputFormat, FileOutputFormat}
import pl.edu.icm.coansys.citations.mappers.{ExactAssesor, HeuristicAdder, CitationExtractor}
import pl.edu.icm.coansys.citations.reducers.BestSelector

/**
 * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
 */
object MatcherLowLevelQD extends Configured with Tool {

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

    val heuristicConf = new Configuration(conf)
    heuristicConf.setInt("mapred.max.split.size", 5 * 1000 * 1000)
    val heuristicJob = new Job(heuristicConf, "Heuristic adder")
    heuristicJob.setJarByClass(getClass)

    FileInputFormat.addInputPath(heuristicJob, new Path(extractedRefsUri))
    FileOutputFormat.setOutputPath(heuristicJob, new Path(refsHeuristicUri))

    heuristicJob.setMapperClass(classOf[HeuristicAdder])
    heuristicJob.setNumReduceTasks(0)
    heuristicJob.setOutputKeyClass(classOf[BytesWritable])
    heuristicJob.setOutputValueClass(classOf[Text])
    heuristicJob.setInputFormatClass(classOf[SequenceFileInputFormat[BytesWritable, BytesWritable]])
    heuristicJob.setOutputFormatClass(classOf[SequenceFileOutputFormat[BytesWritable, Text]])



    if (!heuristicJob.waitForCompletion(true))
      return 1

    val assessorJob = new Job(conf, "Similarity assessor")
    assessorJob.setJarByClass(getClass)

    FileInputFormat.addInputPath(assessorJob, new Path(refsHeuristicUri))
    FileOutputFormat.setOutputPath(assessorJob, new Path(outUri))

    assessorJob.setMapperClass(classOf[ExactAssesor])
    assessorJob.setReducerClass(classOf[BestSelector])
    assessorJob.setCombinerClass(classOf[BestSelector])
    assessorJob.setOutputKeyClass(classOf[Text])
    assessorJob.setOutputValueClass(classOf[Text])
    assessorJob.setInputFormatClass(classOf[SequenceFileInputFormat[BytesWritable, BytesWritable]])
    assessorJob.setOutputFormatClass(classOf[SequenceFileOutputFormat[Text, Text]])

    if (!assessorJob.waitForCompletion(true))
      return 1
    else {
      fs.deleteOnExit(new Path(extractedRefsUri))
      fs.deleteOnExit(new Path(refsHeuristicUri))
      return 0
    }
  }

  def main(args: Array[String]) {
    val exitCode = ToolRunner.run(MatcherLowLevel, args)
    System.exit(exitCode)
  }
}
