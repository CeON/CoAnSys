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

package pl.edu.icm.coansys.citations.indices

import org.apache.hadoop.io.Text

/**
 * A class to make using SimpleIndex[Text, V] easier.
 *
 * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
 */
class SimpleTextIndex[V <: org.apache.hadoop.io.Writable : Manifest](override val indexFileUri: String, override val useDistributedCache: Boolean)
  extends SimpleIndex[Text, V](indexFileUri, useDistributedCache) {
  val text = new Text()

  def get(s: String): V = {
    text.set(s)
    super.get(text) match {
      case Some(bytes) =>
        bytes
      case _ =>
        throw new Exception("No index entry for ---" + s + "---")
    }
  }

}
