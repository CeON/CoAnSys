package pl.edu.icm.coansys.citations.util.sequencefile

import org.testng.annotations.Test
import org.testng.Assert._
import java.io.File
import pl.edu.icm.coansys.commons.scala.automatic_resource_management.using
import com.nicta.scoobi.io.sequence.SeqSchema.StringSchema

/**
 * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
 */
class readWriteTest {
  @Test(groups = Array("slow"))
  def basicTest() {
    val content = List(("k1", "v1"), ("k2", "v2"), ("k3", "v3"))
    val tmpFile = File.createTempFile("readWriteTest", ".sq")
    using(ConvertingSequenceFileWriter.fromLocal[String, String](tmpFile.toURI.toString)) {write =>
      content.foreach(write)
    }
    val result =
      using(ConvertingSequenceFileIterator.fromLocal[String, String](tmpFile.toURI.toString)) {iterator =>
        iterator.toList
      }
    assertEquals(result, content)
  }
}
