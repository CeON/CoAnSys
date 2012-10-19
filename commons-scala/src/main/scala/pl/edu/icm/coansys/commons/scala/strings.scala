package pl.edu.icm.coansys.commons.scala

/**
 * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
 */
object strings {
  /**
   * Returns all rotations of a given string
   *
   * @param s
   * @return
   */
  def rotations(s: String): IndexedSeq[String]= {
    for {
      b <- 0 to (s.length - 1)
      rot = s.substring(b) + s.substring(0, b)
    } yield rot
  }
}
