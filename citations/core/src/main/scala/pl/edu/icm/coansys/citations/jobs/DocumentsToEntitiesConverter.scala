/*
 * This file is part of CoAnSys project.
 * Copyright (c) 20012-2013 ICM-UW
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

package pl.edu.icm.coansys.citations.jobs

import pl.edu.icm.coansys.citations.util.{MyScoobiApp, BytesConverter}
import pl.edu.icm.coansys.models.DocumentProtos.DocumentWrapper
import pl.edu.icm.coansys.citations.data.MatchableEntity
import com.nicta.scoobi.Scoobi._

/**
 * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
 */
object DocumentsToEntitiesConverter extends MyScoobiApp {
  def run() {
    val inUri = args(0)
    val outUri = args(1)

    implicit val converter = new BytesConverter[DocumentWrapper](_.toByteArray, DocumentWrapper.parseFrom)
    val entities = valueFromSequenceFile[DocumentWrapper](inUri)
      .filterNot(_.getDocumentMetadata.getKey.isEmpty)
      .map(x => MatchableEntity.fromDocumentMetadata(x.getDocumentMetadata))
    persist(toSequenceFile(entities.map(ent => (ent.id, ent)), outUri))
  }
}
