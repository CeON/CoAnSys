/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */

package pl.edu.icm.coansys.citations.jobs

import com.nicta.scoobi.application.ScoobiApp
import com.nicta.scoobi.core.DList
import pl.edu.icm.coansys.importers.models.DocumentProtos._
import com.nicta.scoobi.InputsOutputs.convertValueFromSequenceFile
import pl.edu.icm.coansys.citations.util.BytesConverter
import pl.edu.icm.coansys.citations.data.{Entity, DocumentEntity}
import pl.edu.icm.coansys.citations.indices.{SimpleIndex, ApproximateIndex}

/**
 * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
 */
object IndexBuilder extends ScoobiApp {
  override def upload = false

  def readDocsFromSeqFiles(uris: List[String]): DList[Entity] = {
    implicit val converter = new BytesConverter[DocumentWrapper](_.toByteArray, DocumentWrapper.parseFrom(_))
    convertValueFromSequenceFile[DocumentWrapper](uris)
      .map(b => DocumentEntity.fromDocumentMetadata(b.getDocumentMetadata).asInstanceOf[Entity])
  }

  //  def readReferencesFromSeqFiles(uris: List[String]): DList[Entity] = {
  //    implicit val dwConverter = new BytesConverter[DocumentWrapper](_.toByteArray, DocumentWrapper.parseFrom(_))
  //    implicit val rmConverter = new BytesConverter[ReferenceMetadata](_.toByteArray, ReferenceMetadata.parseFrom(_))
  //    convertValueFromSequenceFile[DocumentWrapper](uris)
  //      .flatMap[ReferenceMetadata](b => b.getDocumentMetadata.getReferenceList.toIterable)
  //      .map(CitationEntity.fromUnparsedReferenceMetadata(_))
  //  }

  def run() {
    if (args.length != 3) {
      println("Usage: IndexBuilder [-key|-author] <input_seqfile> <output_index_path>")
    } else {
      args(0) match {
        case "-key" =>
          SimpleIndex.buildKeyIndex(readDocsFromSeqFiles(List(args(1))), args(2))
        case "-author" =>
          ApproximateIndex.buildAuthorIndex(readDocsFromSeqFiles(List(args(1))), args(2))
        case _ =>
          println("Usage: IndexBuilder [-key|-author] <input_seqfile> <output_index_path>")
      }
    }
  }
}
