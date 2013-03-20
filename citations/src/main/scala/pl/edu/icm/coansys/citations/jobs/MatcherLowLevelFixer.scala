/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */

package pl.edu.icm.coansys.citations.jobs

import org.apache.hadoop.mapreduce.{Mapper, Job}
import org.apache.hadoop.io.Text
import org.apache.hadoop.conf.Configured
import org.apache.hadoop.util.{ToolRunner, Tool}
import org.apache.hadoop.fs.Path
import org.apache.hadoop.mapreduce.lib.input.{SequenceFileInputFormat, FileInputFormat}
import org.apache.hadoop.mapreduce.lib.output.{SequenceFileOutputFormat, FileOutputFormat}
import pl.edu.icm.coansys.citations.reducers.BestSelector

/**
 * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
 */
object MatcherLowLevelFixer extends Configured with Tool {

  def run(args: Array[String]): Int = {
    val inUri = args(0)
    val outUri = args(1)
    val conf = getConf

    val assessorJob = new Job(conf, "Similarity assessor - reduce only")
    assessorJob.setJarByClass(getClass)

    FileInputFormat.addInputPath(assessorJob, new Path(inUri))
    FileOutputFormat.setOutputPath(assessorJob, new Path(outUri))

    assessorJob.setMapperClass(classOf[Mapper[Text, Text, Text, Text]])
    assessorJob.setReducerClass(classOf[BestSelector])
    assessorJob.setCombinerClass(classOf[BestSelector])
    assessorJob.setOutputKeyClass(classOf[Text])
    assessorJob.setOutputValueClass(classOf[Text])
    assessorJob.setInputFormatClass(classOf[SequenceFileInputFormat[Text, Text]])
    assessorJob.setOutputFormatClass(classOf[SequenceFileOutputFormat[Text, Text]])

    if (!assessorJob.waitForCompletion(true))
      return 1
    else {
      return 0
    }
  }

  def main(args: Array[String]) {
    val exitCode = ToolRunner.run(MatcherLowLevelFixer, args)
    System.exit(exitCode)
  }
}
