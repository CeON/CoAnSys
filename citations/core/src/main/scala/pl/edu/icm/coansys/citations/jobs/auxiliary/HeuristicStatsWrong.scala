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

package pl.edu.icm.coansys.citations.jobs.auxiliary

import com.nicta.scoobi.Scoobi._
import org.apache.commons.lang.StringUtils
import pl.edu.icm.coansys.citations.util.AugmentedDList.augmentDList
import pl.edu.icm.coansys.citations.indices.EntityIndex
import pl.edu.icm.coansys.citations.util.{nlm, XPathEvaluator}
import org.apache.commons.io.IOUtils
import pl.edu.icm.coansys.citations.data.MatchableEntity

/**
 * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
 */
object HeuristicStatsWrong extends ScoobiApp {
  def run() {
    val indexUri = args(0)
    val inUri = args(1)
    val outUri = args(2)

    val results = fromSequenceFile[String, String](inUri)
      .flatMapWithResource(new EntityIndex(indexUri)) { case (index, (k, v)) =>
        val parts = v.split("\n", 2)
        val ids = parts(0).split(" ").filterNot(StringUtils.isEmpty).map(_.substring(4))
        val xmlString = parts(1)
        if (ids contains k) {
          None
        } else {
          val eval = XPathEvaluator.fromInputStream(IOUtils.toInputStream(xmlString))
          val ref = nlm.referenceMetadataBuilderFromNode(eval.asNode(".")).build()
          val srcCit = MatchableEntity.fromReferenceMetadata(ref)
          val srcDoc = index.getEntityById("doc_" + k)
          val dstDocs = ids.map(id => index.getEntityById("doc_" + id))
          Some((xmlString + "\n\n" + srcCit.toDebugString + "\n\n" + srcDoc.toDebugString,
            dstDocs.map(_.toDebugString).mkString("\n")))
        }
      }

    persist(toSequenceFile(results, outUri, overwrite = true))
  }
}
