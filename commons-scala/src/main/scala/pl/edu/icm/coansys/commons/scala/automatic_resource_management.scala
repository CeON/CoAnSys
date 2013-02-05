/*
 * (C) 2010-2012 ICM UW. All rights reserved.
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
          case _ => //intentionally left blank
        }
      }
    }
  }
}
