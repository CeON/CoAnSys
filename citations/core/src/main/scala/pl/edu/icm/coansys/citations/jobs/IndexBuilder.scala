/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */

package pl.edu.icm.coansys.citations.jobs

import com.nicta.scoobi.application.ScoobiApp
import com.nicta.scoobi.InputsOutputs.convertValueFromSequenceFile
import pl.edu.icm.coansys.citations.data.MatchableEntity
import pl.edu.icm.coansys.citations.indices.{SimpleIndex, ApproximateIndex}
import java.io.File

/**
 * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
 */
object IndexBuilder extends ScoobiApp {
  val myJars = System.getProperty("java.class.path").split(File.pathSeparator)
    .filter(_.toLowerCase.contains(File.separator + "distcache" + File.separator))
    .filter(_.toLowerCase.endsWith(".jar"))

  configuration.addJars(myJars)

  def run() {
    val usage = "Usage: IndexBuilder [-key|-author] <input_seqfile> <output_index_path>"
    if (args.length != 3) {
      println(usage)
    } else {
      args(0) match {
        case "-key" =>
          SimpleIndex.buildKeyIndex(convertValueFromSequenceFile[MatchableEntity](args(1)), args(2))
        case "-author" =>
          ApproximateIndex.buildAuthorIndex(convertValueFromSequenceFile[MatchableEntity](args(1)), args(2))
        case _ =>
          println(usage)
      }
    }
  }
}
