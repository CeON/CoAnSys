package pl.edu.icm.coansys.citations.jobs

import org.apache.hadoop.conf.{Configuration, Configured}
import org.apache.hadoop.util.{ToolRunner, Tool}
import org.apache.hadoop.fs.{Path, FileSystem}
import org.apache.hadoop.mapreduce.{Mapper, Job}
import org.apache.hadoop.mapreduce.lib.input.{MultipleInputs, SequenceFileInputFormat, FileInputFormat}
import org.apache.hadoop.mapreduce.lib.output.{SequenceFileOutputFormat, FileOutputFormat}
import pl.edu.icm.coansys.citations.mappers.{AuthorIndexer, EntityAuthorTagger, HeuristicAdder}
import org.apache.hadoop.io.{Writable, Text, BytesWritable}
import pl.edu.icm.coansys.citations.data._
import pl.edu.icm.coansys.citations.reducers.{AuthorJoinerStepTwo, AuthorJoinerStepOne}

/**
 * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
 */
object Joiner extends Configured with Tool {
  def run(args: Array[String]): Int = {
    val srcUri = args(0)
    val destUri = args(1)
    val outUri = args(2)

    val confOne = getConf

    confOne.setInt("mapred.max.split.size", 5 * 1000 * 1000)
    val jobOne = new Job(confOne, "Joiner step one")
    jobOne.setJarByClass(getClass)

    jobOne.setPartitionerClass(classOf[MarkedTextPartitioner])
    jobOne.setGroupingComparatorClass(classOf[MarkedTextGroupComparator])
    jobOne.setSortComparatorClass(classOf[MarkedTextSortComparator])

    jobOne.setReducerClass(classOf[AuthorJoinerStepOne])

    jobOne.setOutputKeyClass(classOf[MarkedText])
    jobOne.setOutputValueClass(classOf[BytesWritable])
    jobOne.setOutputFormatClass(classOf[SequenceFileOutputFormat[MarkedText, BytesWritable]])

    MultipleInputs.addInputPath(jobOne, new Path(srcUri), classOf[SequenceFileInputFormat[Writable, BytesWritable]], classOf[EntityAuthorTagger])
    MultipleInputs.addInputPath(jobOne, new Path(destUri), classOf[SequenceFileInputFormat[Writable, BytesWritable]], classOf[AuthorIndexer])

    jobOne.setMapOutputKeyClass(classOf[MarkedText])
    jobOne.setMapOutputValueClass(classOf[MarkedBytesWritable])

    FileOutputFormat.setOutputPath(jobOne, new Path(outUri+"_part1"))

    if (!jobOne.waitForCompletion(true))
      return 1

    val confTwo = getConf

    confTwo.setInt("mapred.max.split.size", 5 * 1000 * 1000)
    val jobTwo = new Job(confTwo, "Joiner step two")
    jobTwo.setJarByClass(getClass)

    jobTwo.setMapperClass(classOf[Mapper[MarkedText, BytesWritable, MarkedText, BytesWritable]])

    jobTwo.setMapOutputKeyClass(classOf[MarkedText])
    jobTwo.setMapOutputValueClass(classOf[BytesWritable])
    jobTwo.setInputFormatClass(classOf[SequenceFileInputFormat[MarkedText, BytesWritable]])
    FileInputFormat.addInputPath(jobTwo, new Path(outUri+"_part1"))

    jobTwo.setPartitionerClass(classOf[MarkedTextPartitioner])
    jobTwo.setGroupingComparatorClass(classOf[MarkedTextGroupComparator])
    jobTwo.setSortComparatorClass(classOf[MarkedTextSortComparator])

    jobTwo.setReducerClass(classOf[AuthorJoinerStepTwo])

    jobTwo.setOutputKeyClass(classOf[BytesWritable])
    jobTwo.setOutputValueClass(classOf[BytesWritable])
    jobTwo.setOutputFormatClass(classOf[SequenceFileOutputFormat[BytesWritable, BytesWritable]])
    FileOutputFormat.setOutputPath(jobTwo, new Path(outUri))

    if (jobTwo.waitForCompletion(true))
      0
    else
      1
    
    
  }

  def main(args: Array[String]) {
    val exitCode = ToolRunner.run(Joiner, args)
    System.exit(exitCode)
  }
}


