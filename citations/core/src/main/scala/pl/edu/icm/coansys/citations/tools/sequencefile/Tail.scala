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

package pl.edu.icm.coansys.citations.tools.sequencefile

import org.apache.hadoop.conf.Configuration

/**
 * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
 */
object Tail {
  def main(args: Array[String]) {
    if (args.length != 3) {
      println ("Omits n first records of a sequence file and copies the rest to a new one.")
      println ()
      println ("Usage: Tail <n> <in_sequence_file> <out_sequence_file>")
      System.exit(1)
    }

    val n = args(0).toInt
    val inUri = args(1)
    val outUri = args(2)
    val written = util.transformAndWrite(new Configuration(), inUri, outUri, _.drop(n))

    println("Successfully written " + written + " records")
  }
}
