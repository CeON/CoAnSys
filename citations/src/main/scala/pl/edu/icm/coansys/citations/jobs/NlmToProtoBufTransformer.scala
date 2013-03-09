/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */

package pl.edu.icm.coansys.citations.jobs

import com.nicta.scoobi.application.ScoobiApp
import com.nicta.scoobi.InputsOutputs._
import pl.edu.icm.coansys.citations.util.nlm._
import org.apache.commons.io.IOUtils
import pl.edu.icm.coansys.importers.models.DocumentProtos.{DocumentWrapper, DocumentMetadata}
import com.nicta.scoobi.Persist._
import pl.edu.icm.coansys.citations.util.BytesConverter

/**
 * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
 */
object NlmToProtoBufTransformer extends ScoobiApp {
  override def upload = false

  def run() {
    implicit val wrapperConverter =
      new BytesConverter[DocumentWrapper](_.toByteString.toByteArray, DocumentWrapper.parseFrom(_))
    val nlmUri = args(0)
    val outUri = args(1)
    val nlms = convertFromSequenceFile[String, String](List(nlmUri))
    val converted = nlms.map {
      case (path, xmlString) =>
        val baseMeta = pubmedNlmToProtoBuf(IOUtils.toInputStream(xmlString))
        val docMeta = DocumentMetadata.newBuilder(baseMeta.getDocumentMetadata).setSourcePath(path).build()
        val docWrapper = DocumentWrapper.newBuilder().setRowId(docMeta.getKey).setDocumentMetadata(docMeta).build()
        (xmlString, docWrapper)
    }
    persist(convertToSequenceFile(converted, outUri, overwrite = true))
  }
}
