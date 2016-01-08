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

import pl.edu.icm.coansys.citations.data.MatchableEntity
import pl.edu.icm.coansys.citations.data.entity_id.CitEntityId
import pl.edu.icm.coansys.models.DocumentProtos.ReferenceMetadata


/**
 * Converter of ReferenceMetadata object to MatchableEntity object
 * 
 * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
 * @author madryk
 */
class ReferenceMetadataToEntityConverter(basicMetadataToEntityConverter: BasicMetadataToEntityConverter) {
  
  def this() { this(new BasicMetadataToEntityConverter()) }
  
  
  def convert(meta: ReferenceMetadata): MatchableEntity =
    basicMetadataToEntityConverter.convert(CitEntityId(meta.getSourceDocKey, meta.getPosition).toString, meta.getBasicMetadata)
  
}