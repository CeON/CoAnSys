/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */

package pl.edu.icm.coansys.citations.tools.matcher

import pl.edu.icm.coansys.citations.data.{MatchableEntity, SimilarityMeasurer, Entity}
import pl.edu.icm.coansys.citations.util.{XPathEvaluator, nlm}
import org.apache.commons.io.IOUtils
import org.apache.hadoop.conf.Configuration
import pl.edu.icm.coansys.commons.scala.automatic_resource_management.using
import collection.immutable.Queue
import java.io.{File, FileWriter}
import pl.edu.icm.coansys.citations.util.sequencefile.ConvertingSequenceFileIterator

/**
 * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
 */

object MatcherTrainingFromSeqFile {
  def statefulMap[A, B, S](iterator: Iterator[A], state: S)(fun: (A, S) => (B, S)): Iterator[B] =
    if (!iterator.hasNext)
      Iterator.empty
    else {
      val (newElem, newState) = fun(iterator.next(), state)
      Iterator.single(newElem) ++ statefulMap(iterator, newState)(fun)
    }

  def main(args: Array[String]) {
    val n = 40
    val inUri = args(0)
    val outPath = args(1)
    using(ConvertingSequenceFileIterator.fromUri[String, MatchableEntity](new Configuration(), inUri)) {
      records =>
        val begining = records.take(n).toList
        val measurer = new SimilarityMeasurer
        val featureVectors = statefulMap(records ++ begining.iterator, Queue.empty.enqueue(begining.unzip._2)) {
          case ((xmlString, entity), state: Queue[Entity]) =>
            val eval = XPathEvaluator.fromInputStream(IOUtils.toInputStream(xmlString))
            val refMeta = nlm.referenceMetadataBuilderFromNode(eval.asNode("/ref")).build()
            val cit = MatchableEntity.fromReferenceMetadata(refMeta)
            val featureVectors = (entity :: state.toList).map {
              ent =>
                val features = measurer.featureVectorBuilder.getFeatureVector(ent, cit)
                (entity == ent, features)
            }
            val (_, newState) = state.enqueue(entity).dequeue
            (featureVectors, newState)
        }.flatten
        val lines = featureVectors.map {
          case (equal, fv) =>
            val label = if (equal) 1 else 0
            val features = (Stream.from(1) zip fv.getFeatures).map {
              case (i, v) => i + ":" + v
            }.mkString(" ")
            label + " " + features
        }
        using(new FileWriter(new File(outPath))) {
          writer =>
            lines.foreach(x => writer.write(x + "\n"))
        }
    }
  }
}
