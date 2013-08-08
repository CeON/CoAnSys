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

package pl.edu.icm.coansys.commons.scala

/**
 * This is an implementation of Automatic Resource Management. Based on Martin Odersky's FOSDEM 2009 presentatnio slides.
 *
 * Added resource passing by name so that we can properly handle exceptions during resource creation.
 *
 * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
 */
object automatic_resource_management {
  def using[T <: {def close()}, R](resource: => T)(block: T => R): R = {
    var actualResource: T = null.asInstanceOf[T]
    try {
      actualResource = resource
      block(actualResource)
    } finally {
      if (actualResource != null) {
        try {
          actualResource.close()
        } catch {
          case _: Throwable => //intentionally left blank
        }
      }
    }
  }
}
