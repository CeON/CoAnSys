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
import pl.edu.icm.coansys.citations.indices.EntityIndex
import pl.edu.icm.coansys.citations.util.{MyScoobiApp, nlm, XPathEvaluator}
import pl.edu.icm.coansys.citations.util.classification.svm.SvmClassifier.featureVectorValuesToLibSvmLine
import org.apache.commons.io.IOUtils
import pl.edu.icm.coansys.citations.data.MatchableEntity
import pl.edu.icm.coansys.citations.data.feature_calculators._
import pl.edu.icm.coansys.citations.util.classification.features.FeatureVectorBuilder

/**
 * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
 */
object HeuristicToSvmLight extends MyScoobiApp {
  def run() {
    val indexUri = args(0)
    val inUri = args(1)
    val outUri = args(2)

    val results = fromSequenceFile[String, String](inUri)
      .flatMapWithResource(new EntityIndex(indexUri)) { case (index, (k, v)) =>
        val parts = v.split("\n", 2)
        val ids = parts(0).split(" ").filterNot(StringUtils.isEmpty).map(_.substring(4)).toSet + k
        val xmlString = parts(1)
        val eval = XPathEvaluator.fromInputStream(IOUtils.toInputStream(xmlString))
        val ref = nlm.referenceMetadataBuilderFromNode(eval.asNode(".")).build()
        val srcCit = MatchableEntity.fromReferenceMetadata(ref)
        val dstDocs = ids.toList.map(id => (id == k, index.getEntityById("doc_" + id)))

        val featureVectorBuilder = new FeatureVectorBuilder[(MatchableEntity, MatchableEntity)](List(
          AuthorTrigramMatchFactor,
          AuthorTokenMatchFactor,
          PagesMatchFactor,
          SourceMatchFactor,
          TitleMatchFactor,
          YearMatchFactor))

        (Stream.continually(srcCit) zip dstDocs).map { case (src, (matching, dst)) =>
          val fv = featureVectorBuilder.calculateFeatureVectorValues((src, dst))
          val label = if (matching) 1 else 0
          featureVectorValuesToLibSvmLine(fv, label)
        }
      }

    persist(toTextFile(results, outUri, overwrite = true))
  }
}
