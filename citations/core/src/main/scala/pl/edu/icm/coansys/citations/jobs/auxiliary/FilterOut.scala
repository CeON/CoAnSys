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

/**
 * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
 */
object FilterOut extends ScoobiApp {
  def run() {
    val inFile = args(0)
    val filterOutFile = args(1)
    val outUri = args(2)
    val in = fromSequenceFile[String, String](inFile).map {case (k, v) => (k, Option(v))}
    val filterOut = keyFromSequenceFile[String](filterOutFile).map {case k => (k, Option.empty[String])}
    val result = (in ++ filterOut).groupByKey[String, Option[String]].flatMap {case (k, vsIter) =>
      val vs = vsIter.toList
      if(vs.contains(None))
        Stream.continually(k) zip vs.flatten
      else
        Stream()
    }
    persist(toSequenceFile(result, outUri, overwrite = true))
  }
}
