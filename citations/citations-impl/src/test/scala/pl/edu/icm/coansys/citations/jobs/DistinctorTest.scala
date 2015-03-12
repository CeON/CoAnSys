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

import org.testng.annotations.Test
import org.apache.hadoop.mrunit.mapreduce.MapReduceDriver
import pl.edu.icm.coansys.citations.data.BytesPairWritable
import org.apache.hadoop.io.{BytesWritable, Text}
import pl.edu.icm.coansys.citations.mappers.IdExtractor
import pl.edu.icm.coansys.citations.reducers.Distinctor
import pl.edu.icm.coansys.citations.data.CitationMatchingProtos.MatchableEntityData

/**
 * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
 */
class DistinctorTest {
  @Test(groups = Array("fast"))
  def jobTest() {
    val ent1 = MatchableEntityData.newBuilder().setId("1").build().toByteArray
    val ent2 = MatchableEntityData.newBuilder().setId("2").build().toByteArray
    val ent3 = MatchableEntityData.newBuilder().setId("3").build().toByteArray
    
    new MapReduceDriver[BytesWritable, BytesWritable, Text, BytesPairWritable, BytesWritable, BytesWritable]()
      .withMapper(new IdExtractor)
      .withInput(new BytesWritable(ent1), new BytesWritable(ent2))
      .withInput(new BytesWritable(ent1), new BytesWritable(ent2))
      .withInput(new BytesWritable(ent2), new BytesWritable(ent3))
      .withInput(new BytesWritable(ent1), new BytesWritable(ent2))
      .withInput(new BytesWritable(ent3), new BytesWritable(ent2))
      .withReducer(new Distinctor)
      .withOutput(new BytesWritable(ent1), new BytesWritable(ent2))
      .withOutput(new BytesWritable(ent2), new BytesWritable(ent3))
      .withOutput(new BytesWritable(ent3), new BytesWritable(ent2))
      .runTest()
  }

  @Test(groups = Array("fast"))
  def minOccurrencesTest() {
    val ent1 = MatchableEntityData.newBuilder().setId("1").build().toByteArray
    val ent2 = MatchableEntityData.newBuilder().setId("2").build().toByteArray
    val ent3 = MatchableEntityData.newBuilder().setId("3").build().toByteArray

    val driver = new MapReduceDriver[BytesWritable, BytesWritable, Text, BytesPairWritable, BytesWritable, BytesWritable]()
    driver.getConfiguration.setInt("distinctor.min.occurrences", 3)
    driver
      .withMapper(new IdExtractor)
      .withInput(new BytesWritable(ent1), new BytesWritable(ent2))
      .withInput(new BytesWritable(ent1), new BytesWritable(ent2))
      .withInput(new BytesWritable(ent2), new BytesWritable(ent3))
      .withInput(new BytesWritable(ent1), new BytesWritable(ent2))
      .withInput(new BytesWritable(ent3), new BytesWritable(ent2))
      .withReducer(new Distinctor)
      .withOutput(new BytesWritable(ent1), new BytesWritable(ent2))
      .runTest()
  }

}