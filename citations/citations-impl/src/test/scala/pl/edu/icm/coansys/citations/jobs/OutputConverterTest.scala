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
 * You should have received a copy of the GNU Affero General Public License
 * along with CoAnSys. If not, see <http://www.gnu.org/licenses/>.
 */

package pl.edu.icm.coansys.citations.jobs

import collection.JavaConversions._
import org.apache.hadoop.io.Text
import org.apache.hadoop.mrunit.mapreduce.MapReduceDriver
import org.testng.annotations.Test
import pl.edu.icm.coansys.citations.data.{MatchableEntity, TextWithBytesWritable}
import pl.edu.icm.coansys.citations.mappers
import pl.edu.icm.coansys.citations.reducers
import pl.edu.icm.coansys.models.PICProtos.{Reference, PicOut}
import org.testng.Assert._

class OutputConverterTest {
  val src1_1 = MatchableEntity.fromParameters(id = "cit_src1_1", rawText = "text1_1").data.toByteArray
  val src1_2 = MatchableEntity.fromParameters(id = "cit_src1_2", rawText = "text1_2").data.toByteArray
  val src2_1 = MatchableEntity.fromParameters(id = "cit_src2_1", rawText = "text2_1").data.toByteArray
  val src2_2 = MatchableEntity.fromParameters(id = "cit_src2_2", rawText = "text2_2").data.toByteArray
  val src2_3 = MatchableEntity.fromParameters(id = "cit_src2_3", rawText = "text2_3").data.toByteArray

  @Test(groups = Array("fast"))
  def jobTest() {

    val results = MapReduceDriver.newMapReduceDriver(new mappers.OutputConverter, new reducers.OutputConverter)
      .withInput(new TextWithBytesWritable("cit_src1_1", src1_1), new Text("1.0:doc_dst1"))
      .withInput(new TextWithBytesWritable("cit_src2_3", src2_3), new Text("1.0:doc_dst1"))
      .withInput(new TextWithBytesWritable("cit_src2_1", src2_1), new Text("1.0:doc_dst3"))
      .withInput(new TextWithBytesWritable("cit_src2_2", src2_2), new Text("1.0:doc_dst2"))
      .withInput(new TextWithBytesWritable("cit_src1_2", src1_2), new Text("1.0:doc_dst2"))
      .run()
    assertEquals(results.size(), 2)

    val out1 = PicOut.parseFrom(results.get(0).getSecond.copyBytes)
    val out2 = PicOut.parseFrom(results.get(1).getSecond.copyBytes)

    assertEquals(results.get(0).getFirst.toString, "src1")
    assertEquals(results.get(1).getFirst.toString, "src2")

    assertEquals(out1.getDocId, "src1")
    assertEquals(out2.getDocId, "src2")

    val refs1 = out1.getRefsList.map(x => (x.getRefNum, x.getRawText, x.getDocId)).toSet
    assertEquals(refs1, Set((1, "text1_1", "dst1"), (2, "text1_2", "dst2")))

    val refs2 = out2.getRefsList.map(x => (x.getRefNum, x.getRawText, x.getDocId)).toSet
    assertEquals(refs2, Set((1, "text2_1", "dst3"), (2, "text2_2", "dst2"), (3, "text2_3", "dst1")))
  }
}
