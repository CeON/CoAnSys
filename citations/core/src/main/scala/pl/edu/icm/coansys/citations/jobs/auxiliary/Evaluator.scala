/*
 * This file is part of CoAnSys project.
 * Copyright (c) 2012-2013 ICM-UW
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

package pl.edu.icm.coansys.citations.jobs.auxiliary

import com.nicta.scoobi.Scoobi._
import org.apache.commons.lang.StringUtils
import pl.edu.icm.coansys.citations.util.AugmentedDList.augmentDList
import pl.edu.icm.coansys.citations.util.{NoOpClose, nlm, XPathEvaluator}
import org.apache.commons.io.IOUtils
import pl.edu.icm.coansys.citations.data.{SimilarityMeasurer, MatchableEntity}
import com.nicta.scoobi.lib.Relational

/**
 * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
 */
object Evaluator extends ScoobiApp {
  def run() {
    val indexUri = args(0)
    val inUri = args(1)
    val outUri = args(2)

    val indexList = fromSequenceFile[String, MatchableEntity](indexUri)
    val heuristic = fromSequenceFile[String, String](inUri)
    // citId->citData
    val citations = heuristic.map {
      case (k, v) =>
        println("citations step on " + k)
        val parts = v.split("\n", 2)
        val xmlString = parts(1)
        val eval = XPathEvaluator.fromInputStream(IOUtils.toInputStream(xmlString))
        val ref = nlm.referenceMetadataBuilderFromNode(eval.asNode(".")).build()
        val cit = MatchableEntity.fromReferenceMetadata(ref)
        (k, cit)
    }
    // doc_destDocId->correctId
    val proposed = heuristic.flatMap {
      case (k, v) =>
        println("proposed step on " + k)
        val parts = v.split("\n", 2)
        val ids = parts(0).split(" ").filterNot(StringUtils.isEmpty).toSet
        ids zip Stream.continually(k)
    }

    //citId->heuristicly matched data
    val withData = Relational.joinLeft(proposed, indexList).map {
      case (prefixedDest, (src, Some(entity))) =>
        println("withData on " + prefixedDest)
        //val dest = prefixedDest.substring(4)
        (src, entity)
      case _ => throw new RuntimeException("This should never happen")
    }.groupByKey[String, MatchableEntity]
    val results = Relational.joinLeft(citations, withData).flatMapWithResource(new SimilarityMeasurer with NoOpClose) {
      case (measurer, (key, (cit, Some(iter)))) =>
        println("last step on " + key)
        val best = iter.maxBy(measurer.similarity(_, cit))
        if (best.id.substring(4) != key)
          Some(cit.toDebugString, best.toDebugString)
        else
          None
      case _ => throw new RuntimeException("This should never happen")
    }

    persist(toTextFile(results, outUri, overwrite = true))
  }
}
