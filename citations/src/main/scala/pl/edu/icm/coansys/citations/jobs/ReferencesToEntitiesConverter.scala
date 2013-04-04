/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */

package pl.edu.icm.coansys.citations.jobs

import collection.JavaConversions._
import com.nicta.scoobi.Scoobi._
import pl.edu.icm.coansys.citations.util.AugmentedDList.augmentDList
import pl.edu.icm.coansys.citations.util.{NoOpClose, BytesConverter}
import pl.edu.icm.coansys.importers.models.DocumentProtos.{ReferenceMetadata, DocumentWrapper}
import pl.edu.icm.coansys.citations.data.MatchableEntity
import pl.edu.icm.cermine.bibref.CRFBibReferenceParser

/**
 * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
 */
object ReferencesToEntitiesConverter extends ScoobiApp {
  def run() {
    assert(args(0) == "-model")
    val parserModel = Some(args(1))
    val inUri = args(2)
    val outUri = args(3)

    implicit val dwConverter = new BytesConverter[DocumentWrapper](_.toByteArray, DocumentWrapper.parseFrom(_))
    implicit val rmConverter = new BytesConverter[ReferenceMetadata](_.toByteArray, ReferenceMetadata.parseFrom(_))
    val entities = convertValueFromSequenceFile[DocumentWrapper](inUri)
      .flatMap[ReferenceMetadata](b => b.getDocumentMetadata.getReferenceList.toIterable)
      .flatMapWithResource(new CRFBibReferenceParser(parserModel.get) with NoOpClose) {
      case (parser, meta) if !meta.getRawCitationText.isEmpty =>
        Some(MatchableEntity.fromUnparsedReferenceMetadata(parser, meta))
      case _ =>
        None
    }

    persist(convertToSequenceFile(entities.map(ent => (ent.id, ent)), outUri))
  }
}
