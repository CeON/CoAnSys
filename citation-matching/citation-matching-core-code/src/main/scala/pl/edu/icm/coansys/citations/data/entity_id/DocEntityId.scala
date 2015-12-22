/*
 * This file is part of CoAnSys project.
 * Copyright (c) 2012-2015 ICM-UW
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
 * You should have received a copy of the GNU Affero General Public Licensealong with CoAnSys. If not, see <http://www.gnu.org/licenses/>.
 */

package pl.edu.icm.coansys.citations.data.entity_id

/**
 * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
 */
case class DocEntityId(documentId: String) {
  override def toString: String =
    DocEntityId.prefix + documentId
}

object DocEntityId {
  private val prefix: String = "doc_"

  def fromString(docEntityId: String): DocEntityId = {
    assert(docEntityId.startsWith(prefix))
    val docId: String = docEntityId.substring(prefix.length)

    DocEntityId(docId)
  }
}
