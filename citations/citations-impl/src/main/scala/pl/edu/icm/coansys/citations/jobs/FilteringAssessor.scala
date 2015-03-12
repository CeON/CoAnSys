/*
 * This file is part of CoAnSys project.
 * Copyright (c) 2012-2015 ICM-UW
 *
 * CoAnSys is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * CoAnSys is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with CoAnSys. If not, see <http://www.gnu.org/licenses/>.
 */

package pl.edu.icm.coansys.citations.jobs

import pl.edu.icm.coansys.citations.util.{NoOpClose, MyScoobiApp}
import pl.edu.icm.coansys.citations.util.misc._
import java.util.Locale
import com.nicta.scoobi.Scoobi._
import scala.Some
import pl.edu.icm.coansys.citations.data.{SimilarityMeasurer, MatchableEntity}
import pl.edu.icm.coansys.citations.util.AugmentedDList._

/**
 * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
 */
object FilteringAssessor extends MyScoobiApp {
  def niceTokens(s: String) =
    tokensFromCermine(s.toLowerCase(Locale.ENGLISH)).filter(x => x.length > 2 || x.exists(_.isDigit)).take(50).toSet

  def merge[A](xs: List[(Double, A)], ys: List[(Double, A)], limit: Int): List[(Double, A)] = {
    import scala.util.control.TailCalls._
    def helper: (List[(Double, A)], List[(Double, A)], Int, List[(Double, A)]) => TailRec[List[(Double, A)]] = {
      case (_, _, 0, acc) => done(acc)
      case (Nil, Nil, _, acc) => done(acc)
      case (Nil, ys, limit, acc) => done(ys.take(limit).reverse ::: acc)
      case (xs, Nil, limit, acc) => done(xs.take(limit).reverse ::: acc)
      case (((sx, idx)::tx), ((sy, idy)::ty), limit, acc) =>
        if (sx > sy)
          tailcall(helper(tx, (sy, idy)::ty, limit - 1, (sx, idx)::acc))
        else
          tailcall(helper((sx, idx)::tx, ty, limit - 1, (sy, idy)::acc))
    }
    helper(xs, ys, limit, Nil).result.reverse
  }

  def run() {
    val targetEntitiesUri = args(0)
    val heursUri = args(1)
    val outUri = args(2)

    val heurs = fromSequenceFile[MatchableEntity, String](heursUri)
    val entities = fromSequenceFile[String, MatchableEntity](targetEntitiesUri)

    val filtered = heurs.map(_.swap).joinLeft(entities).mapFlatten{
      case (_, (src, Some(dst))) => Some(src, dst)
      case _ => None
    }.map { case (src, dst) =>
      val srcTokens = niceTokens(src.toReferenceString)
      val dstTokens = niceTokens(dst.toReferenceString)
      (src, List((2.0 * (srcTokens & dstTokens).size / (srcTokens.size + dstTokens.size), dst)))
    }
      .groupByKey[MatchableEntity, List[(Double, MatchableEntity)]]
      .combine(Reduction[List[(Double, MatchableEntity)]]((xs, ys) => merge(xs, ys, 100)))
      .mapFlatten{case (src,list) => Stream.continually(src) zip list.unzip._2}

    val results = filtered.flatMapWithResource(new SimilarityMeasurer with NoOpClose) {case (measurer, (src, dst)) =>
      val minimalSimilarity = 0.5
      val similarity = measurer.similarity(src, dst)
      if (similarity > minimalSimilarity)
        Some((src.id, (similarity, dst.id)))
      else
        None
    }
      .groupByKey[String, (Double, String)]
      .combine(Reduction[(Double, String)]{
        case ((sim1, id), (sim2, _)) if sim1 > sim2 => (sim1, id)
        case (_, pair2) => pair2
      })
      .map {case (src, (sim, dst)) => (src, sim.toString + ":" + dst)}

    persist(results.toSequenceFile[String, String](outUri, overwrite = true))
  }

}
