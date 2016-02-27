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

import scala.collection.JavaConversions.asScalaBuffer

import pl.edu.icm.coansys.citations.data.CitationMatchingProtos.MatchableEntityData
import pl.edu.icm.coansys.citations.data.MatchableEntity
import pl.edu.icm.coansys.models.DocumentProtos.BasicMetadata


/**
 * Converter of BasicMetadata object to MatchableEntity object
 * 
 * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
 * @author madryk
 */
class BasicMetadataToEntityConverter extends Serializable {
  
  def convert(id: String, meta: BasicMetadata): MatchableEntity = {
    val data = MatchableEntityData.newBuilder()
    data.setId(id)
    fillUsingBasicMetadata(data, meta)
    new MatchableEntity(data.build())
  }
  
  
  private def fillUsingBasicMetadata(data: MatchableEntityData.Builder, meta: BasicMetadata) {
    if (meta.getAuthorCount > 0)
      data.setAuthor(meta.getAuthorList.map(a => if (a.hasName) a.getName else a.getForenames + " " + a.getSurname).mkString(", "))
    if (meta.hasJournal)
      data.setSource(meta.getJournal)
    if (meta.getTitleCount > 0)
      data.setTitle(meta.getTitleList.map(_.getText).mkString(". "))
    if (meta.hasPages)
      data.setPages(meta.getPages)
    if (meta.hasIssue)
      data.setIssue(meta.getIssue)
    if (meta.hasVolume)
      data.setVolume(meta.getVolume)
    if (meta.hasYear)
      data.setYear(meta.getYear)
  }
  
}