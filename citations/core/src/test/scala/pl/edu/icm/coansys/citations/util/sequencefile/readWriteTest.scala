/*
 * This file is part of CoAnSys project.
 * Copyright (c) 2012-2013 ICM-UW
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

package pl.edu.icm.coansys.citations.util.sequencefile

import org.testng.annotations.Test
import org.testng.Assert._
import java.io.File
import pl.edu.icm.coansys.commons.scala.automatic_resource_management.using
import com.nicta.scoobi.io.sequence.SeqSchema.StringSchema
import org.apache.commons.io.FileUtils

/**
 * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
 */
class readWriteTest {

  @Test(groups = Array("slow"))
  def basicTest() {
    val content = List(("k1", "v1"), ("k2", "v2"), ("k3", "v3"))
    var tmpFile: File = null
    try {
      tmpFile = File.createTempFile("readWriteTest", ".sq")
      using(ConvertingSequenceFileWriter.fromLocal[String, String](tmpFile.toURI.toString)) {
        write =>
          content.foreach(write)
      }
      val result =
        using(ConvertingSequenceFileIterator.fromLocal[String, String](tmpFile.toURI.toString)) {
          iterator =>
            iterator.toList
        }
      assertEquals(result, content)
    }
    finally {
      FileUtils.deleteQuietly(tmpFile)
    }
  }
}
