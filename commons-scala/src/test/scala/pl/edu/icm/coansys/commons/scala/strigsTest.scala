package pl.edu.icm.coansys.commons.scala

import org.testng.Assert._
import org.testng.annotations.Test
import strings.rotations

/**
 * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
 */
class strigsTest {
  @Test(groups = Array("fast"))
  def rotationsTest() {
    assertEquals(Set("abc", "bca", "cab"), rotations("abc").toSet)
  }
}
