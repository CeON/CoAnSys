package pl.edu.icm.coansys.citations.util.classification.svm

import org.testng.annotations.Test
import org.testng.Assert._

/**
 * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
 */
class SvmClassifierTest {
  @Test(groups = Array("fast"))
  def featureVectorValuesToLibSvmLineTest() {
    val fv = Array(1.0, 0.0)

    val line = SvmClassifier.featureVectorValuesToLibSvmLine(fv, 1)
    assertEquals(line, "1 1:1.0 2:0.0")
  }
}
