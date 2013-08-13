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

import com.nicta.scoobi.application.ScoobiApp
import org.apache.hadoop.fs.Path
import org.apache.hadoop.io.{Text, MapFile}
import pl.edu.icm.coansys.citations.util.BytesIterable

/**
 * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
 */
object IndexStatistics extends ScoobiApp {
  override lazy val upload = false

  def run() {
    val indexFileUri = args(0)
    val reader = new MapFile.Reader(new Path(indexFileUri), configuration.configuration)
    val key = new Text()
    val value = new BytesIterable()
    println("Entries:")
    var noKey = 0
    var noVal = 0
    while (reader.next(key, value)) {
      print(key.toString)
      print("\t")
      println(value.iterable.size)
      noKey += 1
      noVal += value.iterable.size
    }
    println("All entries: " + noKey)
    println("All values: " + noVal)
  }
}
