/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */

package pl.edu.icm.coansys.citations.jobs

import pl.edu.icm.coansys.citations.util.{MyScoobiApp, BytesConverter}
import pl.edu.icm.coansys.models.DocumentProtos.BasicMetadata
import pl.edu.icm.coansys.citations.data.MatchableEntity
import com.nicta.scoobi.Scoobi._

/**
 * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
 */
object BasicMetadataToEntitiesConverter extends MyScoobiApp {

  def run() {
    val inUri = args(0)
    val outUri = args(1)

    implicit val converter = new BytesConverter[BasicMetadata](_.toByteArray, BasicMetadata.parseFrom)
    val entities = fromSequenceFile[String, BasicMetadata](inUri)
      .map {
      case (id, meta) => MatchableEntity.fromBasicMetadata(id, meta)
    }
    persist(entities.map(ent => (ent.id, ent)).toSequenceFile(outUri))
//    persist(toSequenceFile(entities.map(ent => (ent.id, ent)), outUri))
  }
}
