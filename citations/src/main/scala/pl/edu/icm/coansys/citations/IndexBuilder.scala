package pl.edu.icm.coansys.citations

import com.nicta.scoobi.application.ScoobiApp
import com.nicta.scoobi.core.{WireFormat, DList}
import pl.edu.icm.coansys.importers.models.DocumentProtos._
import java.io.{DataOutput, DataInput}

import com.nicta.scoobi.Persist.persist
import com.nicta.scoobi.InputsOutputs.{convertToSequenceFile, toSequenceFile}
import com.nicta.scoobi.io.sequence.SeqSchema
import org.apache.hadoop.io.BytesWritable

/**
 * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
 */
object IndexBuilder extends ScoobiApp {
  override def upload = false

  def doMatching() {
    throw new RuntimeException
  }

  //  def mockReadDocs(): DList[MockDocumentWrapper] = DList.apply[MockDocumentWrapper](
  //    new MockDocumentWrapper("1", "aaa bbb"),
  //    new MockDocumentWrapper("2", "bbb ccc"),
  //    new MockDocumentWrapper("3", "aaa ddd"),
  //    new MockDocumentWrapper("4", "bbb eee"),
  //    new MockDocumentWrapper("5", "abc adb"),
  //    new MockDocumentWrapper("6", "ewr fds"),
  //    new MockDocumentWrapper("7", "sda czx"),
  //    new MockDocumentWrapper("8", "aca bba"),
  //    new MockDocumentWrapper("9", "aa bba")
  //  )
  def mockReadDocs(): DList[DocumentMetadataWrapper] = DList.apply[DocumentMetadataWrapper](
    new DocumentMetadataWrapper(DocumentMetadata.getDefaultInstance)
  )


  def run() {
    println(args)

    ApproximateIndex.buildAuthorIndex(mockReadDocs, args(0))

    //    implicit val documentBytesConverter = new BytesConverter[DocumentMetadata]((_.toByteArray),(DocumentMetadata.parseFrom(_)))
    //    val myList = DList.apply[DocumentMetadata](DocumentMetadata.getDefaultInstance, DocumentMetadata.getDefaultInstance)
    //    val pairs = myList.map(d => (1, d))
    //    persist(convertToSequenceFile(pairs, args(0)))
    //    DocumentMetadata.getDefaultInstance.toBuilder.
    //    doMatching()
  }
}
