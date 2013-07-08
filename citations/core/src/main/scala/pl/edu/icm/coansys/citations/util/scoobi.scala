package pl.edu.icm.coansys.citations.util

import com.nicta.scoobi.Scoobi._
import java.io.File

/**
 * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
 */
object scoobi {
  def addDistCacheJarsToConfiguration(configuration: ScoobiConfiguration) = {
    val classpath = System.getProperty("java.class.path")
    val files = classpath.split(File.pathSeparator).toList
    val jars = files.filter(_.toLowerCase.endsWith(".jar"))
    val jarsFromDistCache = jars.filter(_.toLowerCase.contains(File.separator + "distcache" + File.separator))

    val paramName = "tmpjars"
    val tmpjars = configuration.get(paramName, "").split(",")
    configuration.set(paramName, (tmpjars ++ jarsFromDistCache).mkString(","))
  }
}
