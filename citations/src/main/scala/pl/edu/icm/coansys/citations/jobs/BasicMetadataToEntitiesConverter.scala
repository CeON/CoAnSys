/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */

package pl.edu.icm.coansys.citations.jobs

import pl.edu.icm.coansys.citations.util.BytesConverter
import pl.edu.icm.coansys.importers.models.DocumentProtos.BasicMetadata
import pl.edu.icm.coansys.citations.data.MatchableEntity
import com.nicta.scoobi.Scoobi._

/**
 * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
 */
object BasicMetadataToEntitiesConverter extends ScoobiApp {
  override def includeLibJars = true

  def run() {
    val inUri = args(0)
    val outUri = args(1)

    implicit val converter = new BytesConverter[BasicMetadata](_.toByteArray, BasicMetadata.parseFrom(_))
    val entities = convertFromSequenceFile[String, BasicMetadata](inUri)
      .map {
      case (id, meta) => MatchableEntity.fromBasicMetadata(id, meta)
    }
    persist(convertToSequenceFile(entities.map(ent => (ent.id, ent)), outUri))
  }
}
