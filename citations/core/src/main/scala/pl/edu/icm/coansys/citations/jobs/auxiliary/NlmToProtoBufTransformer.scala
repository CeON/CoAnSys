/*
 * This file is part of CoAnSys project.
 * Copyright (c) 2012-2013 ICM-UW
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

package pl.edu.icm.coansys.citations.jobs.auxiliary

import com.nicta.scoobi.Scoobi._
import pl.edu.icm.coansys.citations.util.nlm._
import org.apache.commons.io.IOUtils
import pl.edu.icm.coansys.models.DocumentProtos.{DocumentWrapper, DocumentMetadata}
import pl.edu.icm.coansys.citations.util.BytesConverter

/**
 * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
 */
object NlmToProtoBufTransformer extends ScoobiApp {
  def run() {
    implicit val wrapperConverter =
      new BytesConverter[DocumentWrapper](_.toByteString.toByteArray, DocumentWrapper.parseFrom)
    val nlmUri = args(0)
    val outUri = args(1)
    val nlms = fromSequenceFile[String, String](List(nlmUri))
    val converted = nlms.map {
      case (path, xmlString) =>
        val baseMeta = pubmedNlmToProtoBuf(IOUtils.toInputStream(xmlString))
        val docMeta = DocumentMetadata.newBuilder(baseMeta.getDocumentMetadata).setSourcePath(path).build()
        val docWrapper = DocumentWrapper.newBuilder().setRowId(docMeta.getKey).setDocumentMetadata(docMeta).build()
        (xmlString, docWrapper)
    }
    persist(toSequenceFile(converted, outUri, overwrite = true))
  }
}
