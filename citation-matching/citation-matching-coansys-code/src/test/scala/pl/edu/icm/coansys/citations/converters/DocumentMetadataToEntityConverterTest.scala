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
import org.testng.Assert._
import org.testng.annotations._

import pl.edu.icm.coansys.citations.data.MatchableEntity
import pl.edu.icm.coansys.models.DocumentProtos.{BasicMetadata, DocumentMetadata}


/**
 * @author madryk
 */
class DocumentMetadataToEntityConverterTest {
  
	val basicMetadataConverter = mock(classOf[BasicMetadataToEntityConverter])
  
  val documentMetadataConverter = new DocumentMetadataToEntityConverter(basicMetadataConverter)
  
  @BeforeMethod
  def setup() {
    reset(basicMetadataConverter)
  }
  
  
  @Test(groups = Array("fast"))
  def convertTest() {
    
    val document = DocumentMetadata.newBuilder()
      .setKey("some_id")
      .setBasicMetadata(BasicMetadata.newBuilder().build())
      .build()
    val entity = MatchableEntity.fromParameters("id", null, null, null, null, null, null)
    
    when(basicMetadataConverter.convert("doc_some_id", document.getBasicMetadata)).thenReturn(entity)
    
    
    val actualEntity = documentMetadataConverter.convert(document)
    
    
    assertTrue(entity == actualEntity)
    
  }
  
}
