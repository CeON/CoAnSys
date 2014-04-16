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

package pl.edu.icm.coansys.citations.mappers

import org.apache.hadoop.io.{NullWritable, BytesWritable}
import org.apache.hadoop.mrunit.mapreduce.MapDriver
import org.junit.Assert.assertEquals
import org.testng.annotations.Test
import pl.edu.icm.coansys.citations.data.{MarkedText, MatchableEntity}

/**
   * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
   */
class DocumentHashGeneratorTest {
    @Test(groups = Array("fast"))
    def mapperByteCopyTest() {
      val entity = MatchableEntity.fromParameters(id = "1", author = "Kowalski", year = "2002", pages = "1-5")
      val driver = MapDriver.newMapDriver(new DocumentHashGenerator)



      driver.addInput(NullWritable.get(), new BytesWritable(entity.data.toByteArray))
      driver.getConfiguration.setBoolean("coansys.citations.mark.documents", false)
      driver.getConfiguration.set("coansys.citations.document.hasher", "pl.edu.icm.coansys.citations.hashers.DocumentHashGenerator")
      driver.addOutput(new MarkedText("kowalski#2001#0#4"), new MarkedText("1"))
      driver.addOutput(new MarkedText("kowalski#2002#0#4"), new MarkedText("1"))
      driver.addOutput(new MarkedText("kowalski#2003#0#4"), new MarkedText("1"))
      driver.addOutput(new MarkedText("kowalski#2001#1#4"), new MarkedText("1"))
      driver.addOutput(new MarkedText("kowalski#2002#1#4"), new MarkedText("1"))
      driver.addOutput(new MarkedText("kowalski#2003#1#4"), new MarkedText("1"))
      driver.addOutput(new MarkedText("kowalski#2001#2#4"), new MarkedText("1"))
      driver.addOutput(new MarkedText("kowalski#2002#2#4"), new MarkedText("1"))
      driver.addOutput(new MarkedText("kowalski#2003#2#4"), new MarkedText("1"))
      driver.addOutput(new MarkedText("kowalski#2001#0#5"), new MarkedText("1"))
      driver.addOutput(new MarkedText("kowalski#2002#0#5"), new MarkedText("1"))
      driver.addOutput(new MarkedText("kowalski#2003#0#5"), new MarkedText("1"))
      driver.addOutput(new MarkedText("kowalski#2001#1#5"), new MarkedText("1"))
      driver.addOutput(new MarkedText("kowalski#2002#1#5"), new MarkedText("1"))
      driver.addOutput(new MarkedText("kowalski#2003#1#5"), new MarkedText("1"))
      driver.addOutput(new MarkedText("kowalski#2001#2#5"), new MarkedText("1"))
      driver.addOutput(new MarkedText("kowalski#2002#2#5"), new MarkedText("1"))
      driver.addOutput(new MarkedText("kowalski#2003#2#5"), new MarkedText("1"))
      driver.addOutput(new MarkedText("kowalski#2001#0#6"), new MarkedText("1"))
      driver.addOutput(new MarkedText("kowalski#2002#0#6"), new MarkedText("1"))
      driver.addOutput(new MarkedText("kowalski#2003#0#6"), new MarkedText("1"))
      driver.addOutput(new MarkedText("kowalski#2001#1#6"), new MarkedText("1"))
      driver.addOutput(new MarkedText("kowalski#2002#1#6"), new MarkedText("1"))
      driver.addOutput(new MarkedText("kowalski#2003#1#6"), new MarkedText("1"))
      driver.addOutput(new MarkedText("kowalski#2001#2#6"), new MarkedText("1"))
      driver.addOutput(new MarkedText("kowalski#2002#2#6"), new MarkedText("1"))
      driver.addOutput(new MarkedText("kowalski#2003#2#6"), new MarkedText("1"))

      driver.runTest(false)
    }
  }
