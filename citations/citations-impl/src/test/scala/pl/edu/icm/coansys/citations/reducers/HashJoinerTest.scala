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

import org.apache.hadoop.io.Text
import org.apache.hadoop.mrunit.mapreduce.ReduceDriver
import org.apache.hadoop.mrunit.types.Pair
import org.testng.annotations.Test
import pl.edu.icm.coansys.citations.data.MarkedText

import scala.collection.JavaConversions._

/**
 * Created by matfed on 28.02.14.
 */
class HashJoinerTest {
  @Test(groups = Array("fast"))
  def basicTest() {
    val key = new MarkedText("key", marked = true)
    val bytesList = List(
      new MarkedText("1*", marked = true),
      new MarkedText("2*", marked = true),
      new MarkedText("3*", marked = true),
      new MarkedText("1"),
      new MarkedText("2"),
      new MarkedText("3"),
      new MarkedText("4")
    )

    val output = List(
      new Pair(new Text("1"), new Text("1*")),
      new Pair(new Text("2"), new Text("1*")),
      new Pair(new Text("3"), new Text("1*")),
      new Pair(new Text("4"), new Text("1*")),
      new Pair(new Text("1"), new Text("2*")),
      new Pair(new Text("2"), new Text("2*")),
      new Pair(new Text("3"), new Text("2*")),
      new Pair(new Text("4"), new Text("2*")),
      new Pair(new Text("1"), new Text("3*")),
      new Pair(new Text("2"), new Text("3*")),
      new Pair(new Text("3"), new Text("3*")),
      new Pair(new Text("4"), new Text("3*"))
    )
    new ReduceDriver[MarkedText, MarkedText, Text, Text]()
      .withReducer(new HashJoiner())
      .withInput(key, bytesList)
      .withAllOutput(output)
      .runTest(false)
  }

  @Test(groups = Array("fast"))
  def thresholdTest() {
    val key = new MarkedText("key", marked = true)
    val bytesList1 = List(
      new MarkedText("1*", marked = true),
      new MarkedText("2*", marked = true),
      new MarkedText("3*", marked = true),
      new MarkedText("1"),
      new MarkedText("2"),
      new MarkedText("3"),
      new MarkedText("4")
    )

    val bytesList2 = List(
      new MarkedText("1*", marked = true),
      new MarkedText("2*", marked = true),
      new MarkedText("1"),
      new MarkedText("2"),
      new MarkedText("3"),
      new MarkedText("4")
    )

    val output = List(
      new Pair(new Text("1"), new Text("1*")),
      new Pair(new Text("2"), new Text("1*")),
      new Pair(new Text("3"), new Text("1*")),
      new Pair(new Text("4"), new Text("1*")),
      new Pair(new Text("1"), new Text("2*")),
      new Pair(new Text("2"), new Text("2*")),
      new Pair(new Text("3"), new Text("2*")),
      new Pair(new Text("4"), new Text("2*"))
    )
    val driver = new ReduceDriver[MarkedText, MarkedText, Text, Text]()
    driver.getConfiguration.setInt("max.documents.per.bucket", 2)
    driver
      .withReducer(new HashJoiner())
      .withInput(key, bytesList1)
      .withInput(key, bytesList2)
      .withAllOutput(output)
      .runTest(false)
  }
}
