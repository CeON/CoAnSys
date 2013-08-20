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

package pl.edu.icm.coansys.citations.jobs

import com.nicta.scoobi.Scoobi._
import pl.edu.icm.coansys.citations.data.MatchableEntity
import pl.edu.icm.coansys.citations.indices.{SimpleIndex, ApproximateIndex}
import pl.edu.icm.coansys.citations.util.MyScoobiApp

/**
 * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
 */
object IndexBuilder extends MyScoobiApp {

  def run() {
    val usage = "Usage: IndexBuilder [-key|-author] <input_seqfile> <output_index_path>"
    if (args.length != 3) {
      println(usage)
    } else {
      args(0) match {
        case "-key" =>
          SimpleIndex.buildKeyIndex(valueFromSequenceFile[MatchableEntity](args(1)), args(2))
        case "-author" =>
          ApproximateIndex.buildAuthorIndex(valueFromSequenceFile[MatchableEntity](args(1)), args(2))
        case _ =>
          println(usage)
      }
    }
  }
}
