/*
 * Copyright (c) 2013-2013 ICM UW
 */

package pl.edu.icm.ceon.scala_commons.classification.svm

import org.junit.Assert._
import org.junit.Test


/**
 * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
 */
class SvmClassifierTest {
  @Test
  def featureVectorValuesToLibSvmLineTest() {
    val fv = Array(1.0, 0.0)

    val line = SvmClassifier.featureVectorValuesToLibSvmLine(fv, 1)
    assertEquals(line, "1 1:1.0 2:0.0")
  }
}
