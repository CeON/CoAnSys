/*
 * This file is part of CoAnSys project.
 * Copyright (c) 2012-2014 ICM-UW
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
 * You should have received a copy of the GNU Affero General Public License
 * along with CoAnSys. If not, see <http://www.gnu.org/licenses/>.
 */

package pl.edu.icm.coansys.citations.jobs

import org.apache.hadoop.io.{NullWritable, Writable, BytesWritable, Text}
import org.apache.hadoop.mrunit.mapreduce.MapReduceDriver
import org.testng.annotations.Test
import pl.edu.icm.coansys.citations.data.BytesPairWritable
import pl.edu.icm.coansys.citations.data.CitationMatchingProtos.MatchableEntityData
import pl.edu.icm.coansys.citations.mappers.{IdCombiner, IdExtractor}
import pl.edu.icm.coansys.citations.reducers.{IdDistinctorExtractor, Distinctor}

/**
 * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
 */
class IdDistinctorTest {
  @Test(groups = Array("fast"))
  def jobTest() {
    val id1 = MatchableEntityData.newBuilder().setId("1").build().toByteArray
    val id2 = MatchableEntityData.newBuilder().setId("2").build().toByteArray
    val id3 = MatchableEntityData.newBuilder().setId("3").build().toByteArray

    new MapReduceDriver[Text, Text, Text, Writable, Text, Text]()
      .withMapper(new IdCombiner)
      .withInput(new Text(id1), new Text(id2))
      .withInput(new Text(id1), new Text(id2))
      .withInput(new Text(id2), new Text(id3))
      .withInput(new Text(id1), new Text(id2))
      .withInput(new Text(id3), new Text(id2))
      .withReducer(new IdDistinctorExtractor)
      .withOutput(new Text(id1), new Text(id2))
      .withOutput(new Text(id2), new Text(id3))
      .withOutput(new Text(id3), new Text(id2))
      .runTest()
  }

  @Test(groups = Array("fast"))
  def minOccurrencesTest() {
    val id1 = MatchableEntityData.newBuilder().setId("1").build().toByteArray
    val id2 = MatchableEntityData.newBuilder().setId("2").build().toByteArray
    val id3 = MatchableEntityData.newBuilder().setId("3").build().toByteArray

    val driver = new MapReduceDriver[Text, Text, Text, Writable, Text, Text]()
    driver.getConfiguration.setInt("distinctor.min.occurrences", 3)
    driver
      .withMapper(new IdCombiner)
      .withInput(new Text(id1), new Text(id2))
      .withInput(new Text(id1), new Text(id2))
      .withInput(new Text(id2), new Text(id3))
      .withInput(new Text(id1), new Text(id2))
      .withInput(new Text(id3), new Text(id2))
      .withReducer(new IdDistinctorExtractor)
      .withOutput(new Text(id1), new Text(id2))
      .runTest()
  }

}