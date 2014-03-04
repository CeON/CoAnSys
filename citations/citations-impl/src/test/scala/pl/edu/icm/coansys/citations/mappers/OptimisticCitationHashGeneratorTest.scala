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

import org.junit.Assert.assertEquals
import org.testng.annotations.Test
import pl.edu.icm.coansys.citations.data.{MarkedText, MatchableEntity}
import org.apache.hadoop.mrunit.mapreduce.MapDriver
import org.apache.hadoop.io.{NullWritable, BytesWritable}

/**
 * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
 */
class OptimisticCitationHashGeneratorTest {
  @Test(groups = Array("fast"))
  def generateTest() {
    val entity = MatchableEntity.fromParameters(id = "1", author = "Jan Kowalski", year = "2002", pages = "1-5")
    val hashes = OptimisticCitationHashGenerator.generate(entity)
    assertEquals(Set("jan#2002#1#5", "kowalski#2002#1#5"), hashes.toSet)
  }

  @Test(groups = Array("fast"))
  def mapperByteCopyTest() {
    val entity = MatchableEntity.fromParameters(id = "1", author = "Jan Kowalski", year = "2002", pages = "1-5")
    val driver = MapDriver.newMapDriver(new OptimisticCitationHashGenerator)

    driver.addInput(NullWritable.get(), new BytesWritable(entity.data.toByteArray))
    driver.getConfiguration.setBoolean("coansys.citations.mark.citations", false)
    driver.addOutput(new MarkedText("jan#2002#1#5"), new MarkedText("1"))
    driver.addOutput(new MarkedText("kowalski#2002#1#5"), new MarkedText("1"))

    driver.runTest(false)
  }
}
