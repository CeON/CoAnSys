package pl.edu.icm.coansys.citations.jobs

import org.apache.hadoop.conf.{Configuration, Configured}
import org.apache.hadoop.util.{ToolRunner, Tool}
import org.apache.hadoop.fs.{Path, FileSystem}
import org.apache.hadoop.mapreduce.Job
import org.apache.hadoop.mapreduce.lib.input.{MultipleInputs, SequenceFileInputFormat, FileInputFormat}
import org.apache.hadoop.mapreduce.lib.output.{SequenceFileOutputFormat, FileOutputFormat}
import pl.edu.icm.coansys.citations.mappers.{AuthorIndexer, EntityAuthorTagger, HeuristicAdder}
import org.apache.hadoop.io.{Writable, Text, BytesWritable}
import pl.edu.icm.coansys.citations.data._
import pl.edu.icm.coansys.citations.reducers.AuthorJoiner

/**
 * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
 */
class Joiner extends Configured with Tool {
  def run(args: Array[String]): Int = {
    val srcUri = args(0)
    val destUri = args(1)
    val outUri = args(2)

    val conf = getConf
    val fs = FileSystem.get(conf)

    conf.setInt("mapred.max.split.size", 5 * 1000 * 1000)
    val job = new Job(conf, "Joiner")
    job.setJarByClass(getClass)

    job.setPartitionerClass(classOf[MarkedTextPartitioner])
    job.setGroupingComparatorClass(classOf[MarkedTextGroupComparator])
    job.setSortComparatorClass(classOf[MarkedTextSortComparator])

    job.setReducerClass(classOf[AuthorJoiner])

    job.setOutputKeyClass(classOf[BytesWritable])
    job.setOutputValueClass(classOf[BytesWritable])
    job.setOutputFormatClass(classOf[SequenceFileOutputFormat[BytesWritable, BytesWritable]])

    MultipleInputs.addInputPath(job, new Path(srcUri), classOf[SequenceFileInputFormat[Writable, BytesWritable]], classOf[EntityAuthorTagger])
    MultipleInputs.addInputPath(job, new Path(srcUri), classOf[SequenceFileInputFormat[Writable, BytesWritable]], classOf[AuthorIndexer])

    job.setMapOutputKeyClass(classOf[MarkedText])
    job.setMapOutputValueClass(classOf[MarkedBytesWritable])

    FileOutputFormat.setOutputPath(job, new Path(outUri))

    if (job.waitForCompletion(true))
      0
    else
      1
  }

  def main(args: Array[String]) {
    val exitCode = ToolRunner.run(MatcherLowLevel, args)
    System.exit(exitCode)
  }
}


