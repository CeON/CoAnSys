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

import scala.collection.JavaConversions._

import pl.edu.icm.cermine.bibref.BibReferenceParser
import pl.edu.icm.cermine.bibref.model.BibEntry
import pl.edu.icm.coansys.citations.data.CitationMatchingProtos.MatchableEntityData
import pl.edu.icm.coansys.citations.data.MatchableEntity
import pl.edu.icm.coansys.citations.data.entity_id.CitEntityId
import pl.edu.icm.coansys.commons.java.DiacriticsRemover.removeDiacritics


/**
 * Converter of raw reference text to MatchableEntity.
 * Internally it uses BibReferenceParser to parse reference.
 * 
 * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
 * @author madryk
 */
class RawReferenceToEntityConverter(referenceParser: BibReferenceParser[BibEntry]) {
  
  
  def convert(id: CitEntityId, rawText: String): MatchableEntity = {
    def getField(bibEntry: BibEntry, key: String, separator: String = " "): String =
      bibEntry.getAllFieldValues(key).mkString(separator)

    val bibEntry = referenceParser.parseBibReference(removeDiacritics(rawText))
    
    val data = MatchableEntityData.newBuilder()
    
    data.setId(id.toString)
    data.setAuthor(getField(bibEntry, BibEntry.FIELD_AUTHOR, ", "))
    data.setSource(getField(bibEntry, BibEntry.FIELD_JOURNAL))
    data.setTitle(getField(bibEntry, BibEntry.FIELD_TITLE))
    data.setPages(getField(bibEntry, BibEntry.FIELD_PAGES))
    data.setYear(getField(bibEntry, BibEntry.FIELD_YEAR))
    data.setVolume(getField(bibEntry, BibEntry.FIELD_VOLUME))
    data.setRawText(rawText)

    new MatchableEntity(data.build())
  }
  
}
