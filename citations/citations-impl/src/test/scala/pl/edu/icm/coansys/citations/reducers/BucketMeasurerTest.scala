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

package pl.edu.icm.coansys.citations.reducers

import java.io.{File, PrintWriter}

import org.apache.hadoop.io.{NullWritable, Text}
import org.apache.hadoop.mrunit.mapreduce.ReduceDriver
import org.apache.hadoop.mrunit.types.Pair
import org.testng.annotations.Test
import pl.edu.icm.coansys.citations.data.MarkedText

import scala.collection.JavaConversions._

/**
 * Created by matfed on 28.02.14.
 */
class BucketMeasurerTest {
  @Test(groups = Array("fast"))
  def thresholdTest() {
    val key1 = new MarkedText("key1", marked = true)
    val bytesList1 = List(
      new MarkedText("1*", marked = true),
      new MarkedText("2*", marked = true),
      new MarkedText("3*", marked = true),
      new MarkedText("1"),
      new MarkedText("2"),
      new MarkedText("3"),
      new MarkedText("4")
    )

    val key2 = new MarkedText("key2", marked = true)
    val bytesList2 = List(
      new MarkedText("1*", marked = true),
      new MarkedText("2*", marked = true),
      new MarkedText("1"),
      new MarkedText("2"),
      new MarkedText("3"),
      new MarkedText("4")
    )

    val output = List(
      new Pair(new Text("key2"), NullWritable.get())
    )
    val driver = new ReduceDriver[MarkedText, MarkedText, Text, NullWritable]()
    driver.getConfiguration.setInt("max.bucket.size", 10)
    driver
      .withReducer(new BucketMeasurer())
      .withInput(key1, bytesList1)
      .withInput(key2, bytesList2)
      .withAllOutput(output)
      .runTest(false)
  }


}
