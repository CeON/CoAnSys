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

import org.testng.annotations.Test

import pl.edu.icm.coansys.citations.converters.MatchableEntityAssert.assertMatchableEntityEquals
import pl.edu.icm.coansys.citations.data.MatchableEntity
import pl.edu.icm.coansys.models.DocumentProtos.{Author, BasicMetadata, TextWithLanguage}


/**
 * @author madryk
 */
class BasicMetadataToEntityConverterTest {
  
  val basicMetadataConverter = new BasicMetadataToEntityConverter() 
  
  
  @Test(groups = Array("fast"))
  def convertTest() {
    
    val basicMetadata = BasicMetadata.newBuilder()
      .addAuthor(Author.newBuilder().setKey("author_id_1").setName("John Doe"))
      .setJournal("Some Journal")
      .addTitle(TextWithLanguage.newBuilder().setText("Some Title"))
      .setPages("12-19")
      .setYear("2001")
      .setIssue("23")
      .setVolume("345")
      .build()
    
    
    val actualEntity = basicMetadataConverter.convert("some_id", basicMetadata)
    
    
    val expectedEntity = MatchableEntity.fromParametersExt("some_id", "John Doe", "Some Journal", "Some Title", "12-19", "2001", "23", "345", null)
    assertMatchableEntityEquals(actualEntity, expectedEntity)
    
  }
  
  
  @Test(groups = Array("fast"))
  def convertEmptyTest() {
    
    val basicMetadata = BasicMetadata.newBuilder()
      .build()
    
    
    val actualEntity = basicMetadataConverter.convert("some_id", basicMetadata)
    
    
    val expectedEntity = MatchableEntity.fromParameters("some_id", null, null, null, null, null, null)
    assertMatchableEntityEquals(actualEntity, expectedEntity)
    
  }
  
  
  @Test(groups = Array("fast"))
  def convertWithMultipleAuthorsTest() {
    
    val basicMetadata = BasicMetadata.newBuilder()
      .addAuthor(Author.newBuilder().setKey("author_id_1").setName("John Doe"))
      .addAuthor(Author.newBuilder().setKey("author_id_2").setName("Jane Doe"))
      .build()
    
    
    val actualEntity = basicMetadataConverter.convert("some_id", basicMetadata)
    
    
    val expectedEntity = MatchableEntity.fromParameters("some_id", "John Doe, Jane Doe", null, null, null, null, null)
    assertMatchableEntityEquals(actualEntity, expectedEntity)
    
  }
  
  
  @Test(groups = Array("fast"))
  def convertWithAuthorForenameAndSurnameTest() {
    
    val basicMetadata = BasicMetadata.newBuilder()
      .addAuthor(Author.newBuilder().setKey("author_id_1").setSurname("Bush").setForenames("George W."))
      .build()
    
    
    val actualEntity = basicMetadataConverter.convert("some_id", basicMetadata)
    
    
    val expectedEntity = MatchableEntity.fromParameters("some_id", "George W. Bush", null, null, null, null, null)
    assertMatchableEntityEquals(actualEntity, expectedEntity)
    
  }
  
  
  @Test(groups = Array("fast"))
  def convertWithMultipleTitlesTest() {
    
    val basicMetadata = BasicMetadata.newBuilder()
      .addTitle(TextWithLanguage.newBuilder().setText("First Title"))
      .addTitle(TextWithLanguage.newBuilder().setText("Second Title"))
      .build()
    
    
    val actualEntity = basicMetadataConverter.convert("some_id", basicMetadata)
    
    
    val expectedEntity = MatchableEntity.fromParameters("some_id", null, null, "First Title. Second Title", null, null, null)
    assertMatchableEntityEquals(actualEntity, expectedEntity)
    
  }
  
  
}
