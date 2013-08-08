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

import com.nicta.scoobi.application.ScoobiApp
import pl.edu.icm.coansys.citations.indices.AuthorIndex

/**
 * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
 */
object IndexTester extends ScoobiApp {
  override lazy val upload = false

  def testIndex(indexUri: String, query: String) {
    val index = new AuthorIndex(indexUri, false)
    index.getDocumentsByAuthor(query).foreach(println)
  }

  def run() {
    if (args.length != 2) {
      println("Usage: IndexTester <index_path> <query>")
    } else {
      testIndex(args(0), args(1))
    }
  }
}
