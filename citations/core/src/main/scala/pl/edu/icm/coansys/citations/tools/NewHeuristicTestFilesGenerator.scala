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
 * You should have received a copy of the GNU Affero General Public Licensealong with CoAnSys. If not, see <http://www.gnu.org/licenses/>.
 */

package pl.edu.icm.coansys.citations.tools

import resource._
import pl.edu.icm.coansys.citations.util.sequencefile.ConvertingSequenceFileWriter
import pl.edu.icm.coansys.citations.data.MatchableEntity

/**
 * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
 */
object NewHeuristicTestFilesGenerator {
  def main(args: Array[String]) {
    val dbUrl = args(0)
    val entitiesUrl = args(1)

    for(writer <- managed(ConvertingSequenceFileWriter.fromLocal[String, MatchableEntity](entitiesUrl))) {
      writer.append("cit_1", MatchableEntity.fromParameters(id = "cit_1", author="Kowalski", year="2000"))
      writer.append("cit_2", MatchableEntity.fromParameters(id = "cit_2", author="Kowalski", year="2001"))
      writer.append("cit_3", MatchableEntity.fromParameters(id = "cit_3", title="Tajemniczy artykuł naukowy", year="2000"))
      writer.append("cit_4", MatchableEntity.fromParameters(id = "cit_4", title="Tajemniczy artykul naukowy", year="1999"))
    }

    for(writer <- managed(ConvertingSequenceFileWriter.fromLocal[String, MatchableEntity](dbUrl))) {
      writer.append("doc_1", MatchableEntity.fromParameters(id = "doc_1", author="Jan Kowalski", title="Tajemniczy artykuł naukowy a zagninięcie Baltazara Gąbki", year="2000"))
    }
  }
}
