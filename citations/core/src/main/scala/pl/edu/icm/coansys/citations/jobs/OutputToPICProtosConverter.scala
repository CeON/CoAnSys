/*
 * This file is part of CoAnSys project.
 * Copyright (c) 2012-2013 ICM-UW
 *
 * CoAnSys is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * CoAnSys is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public Licensealong with CoAnSys. If not, see <http://www.gnu.org/licenses/>.
 */

package pl.edu.icm.coansys.citations.jobs

import com.nicta.scoobi.Scoobi._
import pl.edu.icm.coansys.citations.data.entity_id.{DocEntityId, CitEntityId}
import pl.edu.icm.coansys.citations.util.{BytesConverter, MyScoobiApp}
import pl.edu.icm.coansys.models.PICProtos

/**
 * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
 */
object OutputToPICProtosConverter extends MyScoobiApp {
  def run() {
    val inUrl = args(0)
    val outUrl = args(1)

    val in = fromSequenceFile[String, String](inUrl)

    val grouped = in.map {
      case (src, dst) =>
        val srcId = CitEntityId.fromString(src)
        val dstId = DocEntityId.fromString(dst.split(":", 2)(1))
        (srcId.sourceDocumentId, (srcId.position, dstId.documentId))
    }.groupByKey

    implicit val converter = new BytesConverter[PICProtos.PicOut](_.toByteArray, PICProtos.PicOut.parseFrom)
    val protos = grouped.map {
      case (srcId, matching) =>
        val builder = PICProtos.PicOut.newBuilder()
        builder.setDocId(srcId)
        matching.map {
          case (pos, id) =>
            PICProtos.Reference.newBuilder().setRefNum(pos).setDocId(id).build()
        }.foreach(builder.addRefs)

        (srcId, builder.build())
    }

    persist(protos.toSequenceFile(outUrl))
  }
}
