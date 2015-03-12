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

package pl.edu.icm.coansys.citations.mappers

import org.testng.annotations.Test
import org.apache.hadoop.mrunit.mapreduce.MapDriver
import org.apache.hadoop.io.{Text, BytesWritable}
import pl.edu.icm.coansys.citations.data.{MatchableEntity, BytesPairWritable, TextNumericWritable}

/**
 * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
 */
class LeftEntityExtractorTest {
  val cit1 = MatchableEntity.fromParameters(id = "cit1", rawText = "Jan Kowalski").data.toByteArray
  val doc0 = MatchableEntity.fromParameters(id = "doc0", author = "Alojzy Krasinski").data.toByteArray
  val doc1 = MatchableEntity.fromParameters(id = "doc1", author = "Jan Kowalski").data.toByteArray
  val doc2 = MatchableEntity.fromParameters(id = "doc2", author = "Maciej Nowak").data.toByteArray

  @Test(groups = Array("fast"))
  def mapperByteCopyTest() {
    val driver = MapDriver.newMapDriver(new LeftEntityExtractor)

    driver.addInput(new BytesWritable(cit1), new BytesWritable(doc0))
    driver.addOutput(new Text("cit1"), new BytesWritable(cit1))
    driver.addInput(new BytesWritable(cit1), new BytesWritable(doc1))
    driver.addOutput(new Text("cit1"), new BytesWritable(cit1))
    driver.addInput(new BytesWritable(cit1), new BytesWritable(doc2))
    driver.addOutput(new Text("cit1"), new BytesWritable(cit1))

    driver.runTest()
  }
}
