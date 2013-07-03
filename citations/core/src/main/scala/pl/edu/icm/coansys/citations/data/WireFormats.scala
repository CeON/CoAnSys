package pl.edu.icm.coansys.citations.data

import pl.edu.icm.coansys.citations.util.BytesConverter
import pl.edu.icm.coansys.models.DocumentProtos.{BasicMetadata, DocumentWrapper}

/**
 * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
 */
object WireFormats {
  implicit val docConverter = new BytesConverter[DocumentWrapper](_.toByteArray, DocumentWrapper.parseFrom(_))
  implicit val metaConverter = new BytesConverter[BasicMetadata](_.toByteArray, BasicMetadata.parseFrom(_))
}
