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

/**
 * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
 */
object HeuristicStats extends ScoobiApp {
  override lazy val upload = false

  def run() {
    val inUri = args(0)

    val results = fromSequenceFile[String, String](inUri)
      .map { case (k, v) =>
        (k, v.split("\n", 2)(0).split(" ").filterNot(StringUtils.isEmpty).map(_.substring(4)))
      }
      .map {case (k, v) =>
        val distrib =
          if (v.length > 5000)
            (0,0,0,0,0,0,1)
          else if (v.length > 1000)
            (0,0,0,0,0,1,0)
          else if (v.length > 500)
            (0,0,0,0,1,0,0)
          else if (v.length > 100)
            (0,0,0,1,0,0,0)
          else if (v.length > 50)
            (0,0,1,0,0,0,0)
          else if (v.length > 10)
            (0,1,0,0,0,0,0)
          else
            (1,0,0,0,0,0,0)
        (1, if (v contains k) 1 else 0, v.length, v.length.toLong, distrib)}
      .reduce(Reduction({ case ((count1, contained1, maxLen1, sumLen1, dist1),(count2, contained2, maxLen2, sumLen2, dist2)) =>
        (count1 + count2, contained1 + contained2, maxLen1 max maxLen2, sumLen1 + sumLen2,
          (dist1._1 + dist2._1, dist1._2 + dist2._2, dist1._3 + dist2._3, dist1._4 + dist2._4, dist1._5 + dist2._5, dist1._6 + dist2._6, dist1._7 + dist2._7))
      }))

    val persisted = persist(results)
    println(persisted)
  }
}
