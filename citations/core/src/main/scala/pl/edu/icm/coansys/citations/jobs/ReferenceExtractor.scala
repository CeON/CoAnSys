package pl.edu.icm.coansys.citations.jobs

import collection.JavaConversions._
import com.nicta.scoobi.Scoobi._
import pl.edu.icm.coansys.importers.models.DocumentProtos.DocumentWrapper
import pl.edu.icm.coansys.citations.data.WireFormats._

/**
  * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
  */
object ReferenceExtractor extends ScoobiApp {
   lazy val citationIdPrefix = "cit_"

   def extractReferences(docs: DList[DocumentWrapper]) =
     docs.filterNot(_.getDocumentMetadata.getKey.isEmpty)
       .flatMap{x =>
       val docId = x.getDocumentMetadata.getKey
       x.getDocumentMetadata.getReferenceList.map { ref =>
         (citationIdPrefix + docId + "_" + ref.getPosition, ref.getBasicMetadata)
       }
     }

   def run() {
     val inUri = args(0)
     val outUri = args(1)

     val entities = extractReferences(convertValueFromSequenceFile[DocumentWrapper](inUri))

     persist(convertToSequenceFile(entities, outUri))
   }
 }
