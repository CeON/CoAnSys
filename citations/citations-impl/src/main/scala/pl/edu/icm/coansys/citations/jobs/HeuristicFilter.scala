/*
 * This file is part of CoAnSys project.
 * Copyright (c) 2012-2013 ICM-UW
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

import com.nicta.scoobi.Scoobi._
import pl.edu.icm.coansys.citations.util.MyScoobiApp
import pl.edu.icm.coansys.citations.data.MatchableEntity
import pl.edu.icm.coansys.citations.util.misc._
import java.util.Locale
import scala.annotation.tailrec

/**
 * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
 */
object HeuristicFilter extends MyScoobiApp {
  def niceTokens(s: String) =
    tokensFromCermine(s.toLowerCase(Locale.ENGLISH)).filter(x => x.length > 2 || x.exists(_.isDigit)).take(50).toSet

  def merge(xs: List[(Double, String)], ys: List[(Double, String)], limit: Int): List[(Double, String)] = {
    import scala.util.control.TailCalls._
    def helper: (List[(Double, String)], List[(Double, String)], Int, List[(Double, String)]) => TailRec[List[(Double, String)]] = {
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

    val preassessed = heurs.map(_.swap).joinLeft(entities).mapFlatten{
      case (_, (src, Some(dst))) => Some(src, dst)
      case _ => None
    }.map { case (src, dst) =>
      val srcTokens = niceTokens(src.toReferenceString)
      val dstTokens = niceTokens(dst.toReferenceString)
      (src, List((2.0 * (srcTokens & dstTokens).size / (srcTokens.size + dstTokens.size)).toString, ":", dst.id).mkString)
    }
    persist(preassessed.toSequenceFile[MatchableEntity, String](outUri + "_preassessed", overwrite = true))

    val filtered =
    fromSequenceFile[MatchableEntity, String](outUri + "_preassessed").map{case (src, value) =>
      val Array(scoreStr, dst) = value.split(":", 2)
      (src, List((scoreStr.toDouble, dst)))
    }
     .groupByKey[MatchableEntity, List[(Double, String)]]
     .combine(Reduction[List[(Double, String)]]((xs, ys) => merge(xs, ys, 100)))
     .mapFlatten{case (src,list) => Stream.continually(src) zip list.unzip._2}

    persist(filtered.toSequenceFile[MatchableEntity, String](outUri, overwrite = true))
  }
}
