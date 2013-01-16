/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */

package pl.edu.icm.coansys.citations.jobs

import scala.collection.JavaConversions._
import com.nicta.scoobi.application.ScoobiApp
import com.nicta.scoobi.InputsOutputs._
import com.nicta.scoobi.Persist._
import pl.edu.icm.coansys.importers.models.DocumentProtos.DocumentWrapper
import pl.edu.icm.coansys.importers.models.PICProtos
import pl.edu.icm.coansys.citations.util.{misc, BytesConverter}

/**
 * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
 */
object MockOutputCreator extends ScoobiApp {
  override def upload = false

  def run() {
    val in = args(0)
    val out = args(1)
    implicit val wrapperConverter = new BytesConverter[DocumentWrapper](_.toByteArray, DocumentWrapper.parseFrom(_))
    implicit val picOutConverter = new BytesConverter[PICProtos.PicOut](_.toByteArray, PICProtos.PicOut.parseFrom(_))
    implicit val stringConverter = new BytesConverter[String](misc.uuidEncode, misc.uuidDecode)
    val result = convertValueFromSequenceFile[DocumentWrapper](List(in))
      .map {
      wrapper =>
        val doc = wrapper.getDocumentMetadata
        val outBuilder = PICProtos.PicOut.newBuilder()
        outBuilder.setDocId(doc.getExtId(0).getValue)
        for (ref <- doc.getReferenceList.toIterable) {
          outBuilder.addRefs(PICProtos.Reference.newBuilder().setDocId(doc.getExtId(0).getValue).setRefNum(ref.getPosition))
        }

        (doc.getKey, outBuilder.build())
    }
    persist(convertToSequenceFile(result, out))
  }
}
