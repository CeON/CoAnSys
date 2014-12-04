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

package pl.edu.icm.coansys.citations.jobs

import com.nicta.scoobi.Scoobi._
import pl.edu.icm.coansys.citations.util.AugmentedDList.augmentDList
import pl.edu.icm.coansys.citations.util.{MyScoobiApp, NoOpClose}
import pl.edu.icm.coansys.citations.data.MatchableEntity
import pl.edu.icm.cermine.bibref.{BibReferenceParser, CRFBibReferenceParser}
import pl.edu.icm.cermine.bibref.model.BibEntry

/**
 * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
 */
object ReferencesToEntitiesConverter extends MyScoobiApp {
  def maxSupportedCitationLength = 2000

  def run() {
    var parser: (()=> BibReferenceParser[BibEntry] with NoOpClose) = null
    var inUri: String = null
    var outUri: String = null
    if(args(0) == "-model") {
      parser = () => new CRFBibReferenceParser(args(1)) with NoOpClose
      inUri = args(2)
      outUri = args(3)
    }
    else {
      parser = () => new CRFBibReferenceParser(
        this.getClass.getResourceAsStream("/pl/edu/icm/cermine/bibref/acrf-small.ser.gz")) with NoOpClose
      inUri = args(0)
      outUri = args(1)
    }

    val entities = fromSequenceFile[String, String](inUri)
      .flatMapWithResource(parser()) {
      case (the_parser, (id, text)) if !text.isEmpty && text.length <= maxSupportedCitationLength =>
        try {
          Some(id, MatchableEntity.fromUnparsedReference(the_parser, id, text))
        } catch {
          case e:Exception =>
            System.err.println("Error while parsing " + text)
            e.printStackTrace(System.err)
            None
        }
      case _ =>
        None
    }

    persist(toSequenceFile(entities, outUri))
  }
}
