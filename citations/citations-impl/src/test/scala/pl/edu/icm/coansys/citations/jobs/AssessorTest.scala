/*
 * This file is part of CoAnSys project.
 * Copyright (c) 2012-2015 ICM-UW
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

import org.apache.hadoop.io.BytesWritable
import org.apache.hadoop.mrunit.mapreduce.MapReduceDriver
import org.testng.Assert._
import org.testng.annotations.Test
import pl.edu.icm.coansys.citations.data.{TextWithBytesWritable, MatchableEntity}
import pl.edu.icm.coansys.citations.mappers.EntityAssesor
import pl.edu.icm.coansys.citations.reducers.BestSelector


/**
 * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
 */
class AssessorTest {
  val cit1 = MatchableEntity.fromParameters(
    id = "cit1",
    author = "Kogut, J. Ciurej, H.",
    source = "Mechanika",
    title = "Numerical modelling of the train-track axle forces",
    pages = "209--218",
    rawText = "Kogut, J. and Ciurej, H. (2005a). Numerical modelling of the train-track axle forces, Mechanika 222(65): 209-218."
  ).data.toByteArray
  val doc1 = MatchableEntity.fromParameters(
    id = "doc1",
    author = "Kogut, J. Ciurej, H.",
    source = "Zeszyty Naukowe Politechniki Rzeszowskiej. Mechanika",
    title = "Numerical modelling of the train-track axle forces",
    pages = "209-218",
    year = "2005"
  ).data.toByteArray
  val doc0 = MatchableEntity.fromParameters(
    id = "doc0",
    author = "Kogut, J. Ciurej, H.",
    source = "Czasopismo Techniczne. Budownictwo",
    title = "The numerical study of the dynamic train axle forces induced on an uneven railway track " +
      "Modelowanie numeryczne sil dynamicznych wzbudzonych przez nierownosci szyn na styku pojazd szynowy-tor jazdy",
    pages = "35-49",
    year = "2005"
  ).data.toByteArray

  @Test(groups = Array("fast"))
  def jobTest() {

    val driver = MapReduceDriver.newMapReduceDriver(new EntityAssesor, new BestSelector[TextWithBytesWritable], new BestSelector[TextWithBytesWritable])
      .withInput(new BytesWritable(cit1), new BytesWritable(doc0))
      .withInput(new BytesWritable(cit1), new BytesWritable(doc1))
      .withInput(new BytesWritable(cit1), new BytesWritable(doc0))

    val results = driver.run()
    assertEquals(results.size(), 1)
    val elem = results.get(0)
    assertEquals(elem.getFirst.toString, "cit1")
    assertEquals(elem.getSecond.toString.split(":", 2)(1), "doc1")
  }
}
