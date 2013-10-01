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

/**
 * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
 */
object HeuristicFilter extends MyScoobiApp {
  def niceTokens(s: String) =
    tokensFromCermine(s.toLowerCase(Locale.ENGLISH)).filter(x => x.length > 2 || x.exists(_.isDigit)).take(50).toSet

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
      (src, (2.0 * (srcTokens & dstTokens).size / (srcTokens | dstTokens).size, dst.id))
    }.groupByKey[MatchableEntity, (Double, String)].mapFlatten { case (src, iter) =>
      for ((score, id) <- nGreatest(iter, 100))
        yield (src, id)
    }

    persist(filtered.toSequenceFile[MatchableEntity, String](outUri, overwrite = true))
  }
}
