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

package pl.edu.icm.coansys.citations.hashers

import org.apache.hadoop.io.{NullWritable, BytesWritable}
import org.apache.hadoop.mrunit.mapreduce.MapDriver
import org.junit.Assert.assertEquals
import org.testng.annotations.Test
import pl.edu.icm.coansys.citations.data.{MarkedText, MatchableEntity}
import pl.edu.icm.coansys.citations.mappers.CitationHashGenerator

/**
 * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
 */
class CitationNameYearPagesOptimisticHashGeneratorTest {
  @Test(groups = Array("fast"))
  def generateTest() {
    val entity = MatchableEntity.fromParameters(id = "1", author = "Jan Kowalski", year = "2002", pages = "1-5")
    val hashes = new CitationNameYearPagesOptimisticHashGenerator().generate(entity)
    assertEquals(Set("jan#2002#1#5", "kowalski#2002#1#5"), hashes.toSet)
  }
}
