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

package pl.edu.icm.coansys.citations.reducers

import org.testng.annotations.Test
import org.apache.hadoop.mrunit.mapreduce.{MapReduceDriver, ReduceDriver}
import pl.edu.icm.coansys.citations.data.{MatchableEntity, TextNumericWritable, BytesPairWritable}
import org.apache.hadoop.io.{Text, BytesWritable}
import org.apache.hadoop.mapreduce.Mapper

/**
 * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
 */
class XorTest {
  val cit0 = MatchableEntity.fromParameters(id = "cit0", rawText = "Alojzy Krasinski").data.toByteArray
  val cit1 = MatchableEntity.fromParameters(id = "cit1", rawText = "Jan Kowalski").data.toByteArray
  val cit2 = MatchableEntity.fromParameters(id = "cit2", rawText = "Maciej Nowak").data.toByteArray

  @Test(groups = Array("fast"))
  def basicTest() {
    val driver = MapReduceDriver.newMapReduceDriver(new Mapper[Text, BytesWritable, Text, BytesWritable], new Xor[Text, BytesWritable])

    driver.addInput(new Text("cit0"), new BytesWritable(cit0))
    driver.addInput(new Text("cit1"), new BytesWritable(cit1))
    driver.addInput(new Text("cit2"), new BytesWritable(cit2))
    driver.addInput(new Text("cit0"), new BytesWritable(cit0))
    driver.addInput(new Text("cit2"), new BytesWritable(cit2))
    driver.addInput(new Text("cit0"), new BytesWritable(cit0))
    driver.addOutput(new Text("cit1"), new BytesWritable(cit1))

    driver.runTest()
  }
}
