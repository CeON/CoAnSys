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

import org.mockito.Mockito._
import org.testng.annotations._

import pl.edu.icm.cermine.bibref.BibReferenceParser
import pl.edu.icm.cermine.bibref.model.BibEntry
import pl.edu.icm.coansys.citations.converters.MatchableEntityAssert.assertMatchableEntityEquals
import pl.edu.icm.coansys.citations.data.MatchableEntity
import pl.edu.icm.coansys.citations.data.entity_id.CitEntityId


/**
 * @author madryk
 */
class RawReferenceToEntityConverterTest {
  
  val referenceParser: BibReferenceParser[BibEntry] = mock(classOf[BibReferenceParser[BibEntry]])
  
  val rawReferenceToEntityConverter: RawReferenceToEntityConverter = new RawReferenceToEntityConverter(referenceParser)
  
  @BeforeMethod
  def setup() {
    reset(referenceParser)
  }
  
  
  @Test(groups = Array("fast"))
  def convertTest() {
    
    // given
    
    val bibEntry = new BibEntry()
    bibEntry.setField(BibEntry.FIELD_AUTHOR, "John Doe")
    bibEntry.setField(BibEntry.FIELD_JOURNAL, "Some Journal")
    bibEntry.setField(BibEntry.FIELD_TITLE, "Some Title")
    bibEntry.setField(BibEntry.FIELD_PAGES, "15-32")
    bibEntry.setField(BibEntry.FIELD_YEAR, "2005")
    bibEntry.setField(BibEntry.FIELD_VOLUME, "22")
    
    when(referenceParser.parseBibReference("reference text")).thenReturn(bibEntry)
    
    
    // execute
    
    val entity = rawReferenceToEntityConverter.convert(new CitEntityId("id", 12), "reference text")
    
    
    // assert
    
    val expectedEntity = MatchableEntity.fromParametersExt("cit_id_12", "John Doe", "Some Journal", "Some Title", "15-32", "2005", null, "22", "reference text")
    assertMatchableEntityEquals(entity, expectedEntity)
    verify(referenceParser).parseBibReference("reference text")
  }
  
  
  @Test(groups = Array("fast"))
  def convertEmptyTest() {
    
    // given
    
    val bibEntry = new BibEntry()
    when(referenceParser.parseBibReference("reference text")).thenReturn(bibEntry)
    
    
    // execute
    
    val entity = rawReferenceToEntityConverter.convert(new CitEntityId("id", 12), "reference text")
    
    
    // assert
    
    val expectedEntity = MatchableEntity.fromParameters("cit_id_12", null, null, null, null, null, "reference text")
    assertMatchableEntityEquals(entity, expectedEntity)
    verify(referenceParser).parseBibReference("reference text")
  }
  
  
  @Test(groups = Array("fast"))
  def convertMultipleAuthorsTest() {
    
    // given
    
    val bibEntry = new BibEntry()
    bibEntry.addField(BibEntry.FIELD_AUTHOR, "John Doe")
    bibEntry.addField(BibEntry.FIELD_AUTHOR, "Jane Doe")
    when(referenceParser.parseBibReference("reference text")).thenReturn(bibEntry)
    
    
    // execute
    
    val entity = rawReferenceToEntityConverter.convert(new CitEntityId("id", 12), "reference text")
    
    
    // assert
    
    val expectedEntity = MatchableEntity.fromParameters("cit_id_12", "John Doe, Jane Doe", null, null, null, null, "reference text")
    assertMatchableEntityEquals(entity, expectedEntity)
    verify(referenceParser).parseBibReference("reference text")
  }
  
  
  @Test(groups = Array("fast"))
  def convertMultipleTitleTest() {
    
    // given
    
    val bibEntry = new BibEntry()
    bibEntry.addField(BibEntry.FIELD_TITLE, "First Title")
    bibEntry.addField(BibEntry.FIELD_TITLE, "Second Title")
    when(referenceParser.parseBibReference("reference text")).thenReturn(bibEntry)
    
    
    // execute
    
    val entity = rawReferenceToEntityConverter.convert(new CitEntityId("id", 12), "reference text")
    
    
    // assert
    
    val expectedEntity = MatchableEntity.fromParameters("cit_id_12", null, null, "First Title Second Title", null, null, "reference text")
    assertMatchableEntityEquals(entity, expectedEntity)
    verify(referenceParser).parseBibReference("reference text")
  }
  
  
  @Test(groups = Array("fast"))
  def convertWithDiactricsTest() {
    
    // given
    
    val bibEntry = new BibEntry()
    when(referenceParser.parseBibReference("AE ss aelozzcsn cou")).thenReturn(bibEntry)
    
    
    // execute
    
    val entity = rawReferenceToEntityConverter.convert(new CitEntityId("id", 12), "Æ ß ąęłóżźćśń çöü")
    
    
    // assert
    
    val expectedEntity = MatchableEntity.fromParameters("cit_id_12", null, null, null, null, null, "Æ ß ąęłóżźćśń çöü")
    assertMatchableEntityEquals(entity, expectedEntity)
    verify(referenceParser).parseBibReference("AE ss aelozzcsn cou")
  }
  
}
