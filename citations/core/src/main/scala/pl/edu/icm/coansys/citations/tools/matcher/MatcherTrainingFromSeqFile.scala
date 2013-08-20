/*
 * This file is part of CoAnSys project.
 * Copyright (c) 20012-2013 ICM-UW
 * 
 * CoAnSys is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.

 * CoAnSys is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with CoAnSys. If not, see <http://www.gnu.org/licenses/>.
 */

package pl.edu.icm.coansys.citations.tools.matcher

import pl.edu.icm.coansys.citations.data.{MatchableEntity, SimilarityMeasurer}
import pl.edu.icm.coansys.citations.util.{XPathEvaluator, nlm}
import org.apache.commons.io.IOUtils
import org.apache.hadoop.conf.Configuration
import pl.edu.icm.coansys.commons.scala.automatic_resource_management.using
import collection.immutable.Queue
import java.io.{File, FileWriter}
import pl.edu.icm.coansys.citations.util.sequencefile.ConvertingSequenceFileIterator
import pl.edu.icm.coansys.citations.util.libsvm_util.featureVectorToLibSvmLine

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
          case ((xmlString, entity), state: Queue[MatchableEntity]) =>
            val eval = XPathEvaluator.fromInputStream(IOUtils.toInputStream(xmlString))
            val refMeta = nlm.referenceMetadataBuilderFromNode(eval.asNode("/ref")).build()
            val cit = MatchableEntity.fromReferenceMetadata(refMeta)
            val featureVectors = (entity :: state.toList).map {
              ent =>
                val features = measurer.featureVectorBuilder.calculateFeatureVectorValues((ent, cit))
                (entity == ent, features)
            }
            val (_, newState) = state.enqueue(entity).dequeue
            (featureVectors, newState)
        }.flatten
        val lines = featureVectors.map {
          case (equal, fv) =>
            val label = if (equal) 1 else 0
            featureVectorToLibSvmLine(fv, label)
        }
        using(new FileWriter(new File(outPath))) {
          writer =>
            lines.foreach(x => writer.write(x + "\n"))
        }
    }
  }
}
