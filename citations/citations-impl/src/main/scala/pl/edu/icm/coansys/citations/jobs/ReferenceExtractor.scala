/*
 * This file is part of CoAnSys project.
 * Copyright (c) 2012-2015 ICM-UW
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

import collection.JavaConversions._
import com.nicta.scoobi.Scoobi._
import pl.edu.icm.coansys.models.DocumentProtos.DocumentWrapper
import pl.edu.icm.coansys.citations.data.WireFormats._
import java.io.File
import pl.edu.icm.coansys.citations.util.MyScoobiApp

/**
  * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
  */
object ReferenceExtractor extends MyScoobiApp {
  lazy val citationIdPrefix = "cit_"

   def extractReferences(docs: DList[DocumentWrapper]) =
     docs.filterNot(_.getDocumentMetadata.getKey.isEmpty)
       .mapFlatten{x =>
       val docId = x.getDocumentMetadata.getKey
    //TODO: make another way of storing position whileit is extracted later in CitEntityId   
    
       x.getDocumentMetadata.getReferenceList.map { ref =>
         (citationIdPrefix + docId + "_" + ref.getPosition, ref.getRawCitationText)
       }
     }

   def run() {
     val inUri = args(0)
     val outUri = args(1)

     val entities = extractReferences(valueFromSequenceFile[DocumentWrapper](inUri))

     entities.toSequenceFile(outUri).persist
   }
 }
