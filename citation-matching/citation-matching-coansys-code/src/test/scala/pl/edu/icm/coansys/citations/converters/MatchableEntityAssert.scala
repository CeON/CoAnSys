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

package pl.edu.icm.coansys.citations.converters

import org.testng.Assert.assertEquals

import pl.edu.icm.coansys.citations.data.MatchableEntity


/**
 * Assertions for MatchableEntity objects
 * 
 * @author madryk
 */
object MatchableEntityAssert {
  
  
  def assertMatchableEntityEquals(actual: MatchableEntity, expected: MatchableEntity) = {
    assertEquals(actual.id, expected.id)
    assertEquals(actual.author, expected.author)
    assertEquals(actual.source, expected.source)
    assertEquals(actual.title, expected.title)
    assertEquals(actual.pages, expected.pages)
    assertEquals(actual.year, expected.year)
    assertEquals(actual.issue, expected.issue)
    assertEquals(actual.volume, expected.volume)
    assertEquals(actual.rawText, expected.rawText)
  }
  
}