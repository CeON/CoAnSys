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
import pl.edu.icm.cermine.bibref.{BibReferenceParser, CRFBibReferenceParser}
import pl.edu.icm.cermine.bibref.model.BibEntry

/**
 * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
 */
object ReferencesToEntitiesConverter extends ScoobiApp {
  override def upload = false

  def run() {
    var parser: (=> BibReferenceParser[BibEntry] with NoOpClose) = null
    var inUri: String = null
    var outUri: String = null
    if(args(0) == "-model") {
      parser = new CRFBibReferenceParser(args(1)) with NoOpClose
      inUri = args(2)
      outUri = args(3)
    }
    else {
      parser = new CRFBibReferenceParser(
        this.getClass.getResourceAsStream("/pl/edu/icm/cermine/bibref/acrf-small.ser.gz")) with NoOpClose
      inUri = args(0)
      outUri = args(1)
    }

    implicit val dwConverter = new BytesConverter[DocumentWrapper](_.toByteArray, DocumentWrapper.parseFrom(_))
    implicit val rmConverter = new BytesConverter[ReferenceMetadata](_.toByteArray, ReferenceMetadata.parseFrom(_))
    val entities = convertValueFromSequenceFile[DocumentWrapper](inUri)
      .flatMap[ReferenceMetadata](b => b.getDocumentMetadata.getReferenceList.toIterable)
      .flatMapWithResource(parser) {
      case (parser, meta) if !meta.getRawCitationText.isEmpty =>
        Some(MatchableEntity.fromUnparsedReferenceMetadata(parser, meta))
      case _ =>
        None
    }

    persist(convertToSequenceFile(entities.map(ent => (ent.id, ent)), outUri))
  }
}
