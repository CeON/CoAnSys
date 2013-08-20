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

package pl.edu.icm.coansys.citations.indices

import pl.edu.icm.coansys.citations.util.{misc, BytesIterable}

/**
 * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
 */
class AuthorIndex(val indexFileUri: String, val useDistributedCache: Boolean = true) {
  private val index = new ApproximateIndex[BytesIterable](indexFileUri, useDistributedCache)

  def getDocumentsByAuthor(author: String): Iterable[String] = {
    index.getApproximate(author).flatMap(_.iterable map misc.uuidDecode).toSet
  }

  def close() {
    index.close()
  }

}
