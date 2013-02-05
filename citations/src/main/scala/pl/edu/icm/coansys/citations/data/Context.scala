/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */

package pl.edu.icm.coansys.citations.data

import pl.edu.icm.cermine.bibref.BibReferenceParser
import pl.edu.icm.cermine.bibref.model.BibEntry

/**
 * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
 */
object Context {
  def getEntityById(s: String): Entity = null

  def similarityComputator(features: List[Double]): Double = {
    0.0
  }

  val bibReferenceParser: BibReferenceParser[BibEntry] = null
}
