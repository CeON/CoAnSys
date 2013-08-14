/*
 * This file is part of CoAnSys project.
 * Copyright (c) 2012-2013 ICM-UW
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

package pl.edu.icm.coansys.citations.data

import pl.edu.icm.coansys.citations.util.BytesConverter
import pl.edu.icm.coansys.models.DocumentProtos.{BasicMetadata, DocumentWrapper}
import pl.edu.icm.coansys.models.PICProtos

/**
 * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
 */
object WireFormats {
  implicit val docConverter = new BytesConverter[DocumentWrapper](_.toByteArray, DocumentWrapper.parseFrom)
  implicit val metaConverter = new BytesConverter[BasicMetadata](_.toByteArray, BasicMetadata.parseFrom)
  implicit val picConverter = new BytesConverter[PICProtos.PicOut](_.toByteArray, PICProtos.PicOut.parseFrom)
}
