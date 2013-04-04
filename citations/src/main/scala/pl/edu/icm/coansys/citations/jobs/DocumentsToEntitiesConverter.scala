/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */

package pl.edu.icm.coansys.citations.jobs

import pl.edu.icm.coansys.citations.util.BytesConverter
import pl.edu.icm.coansys.importers.models.DocumentProtos.DocumentWrapper
import pl.edu.icm.coansys.citations.data.MatchableEntity
import com.nicta.scoobi.Scoobi._

/**
 * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
 */
object DocumentsToEntitiesConverter extends ScoobiApp {
  def run() {
    val inUri = args(0)
    val outUri = args(1)

    implicit val converter = new BytesConverter[DocumentWrapper](_.toByteArray, DocumentWrapper.parseFrom(_))
    val entities = convertValueFromSequenceFile[DocumentWrapper](inUri)
      .filterNot(_.getDocumentMetadata.getKey.isEmpty)
      .map(x => MatchableEntity.fromDocumentMetadata(x.getDocumentMetadata))
    persist(convertToSequenceFile(entities.map(ent => (ent.id, ent)), outUri))
  }
}
