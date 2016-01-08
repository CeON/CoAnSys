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
case class CitEntityId(sourceDocumentId: String, position: Int) {
  override def toString =
    CitEntityId.prefix + sourceDocumentId + CitEntityId.separator + position
}

object CitEntityId {
  private val prefix: String = "cit_"
  private val separator: String = "_"

  def fromString(citId: String) = {
    assert(citId.startsWith(prefix))
    val slices: Array[String] = citId.substring(prefix.length).split(separator, 2)
    val src: String = slices(0)
    val pos: Int = slices(1).toInt

    CitEntityId(src, pos)
  }
}
