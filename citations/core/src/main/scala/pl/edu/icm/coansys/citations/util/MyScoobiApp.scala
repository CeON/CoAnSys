package pl.edu.icm.coansys.citations.util

import com.nicta.scoobi.application.ScoobiApp
import com.nicta.scoobi.impl.ScoobiConfiguration._
import com.nicta.scoobi.impl.ScoobiConfigurationImpl._
import com.nicta.scoobi.core.ScoobiConfiguration
import org.apache.hadoop.filecache.DistributedCache
import org.apache.hadoop.fs.Path
import java.io.File

/**
 * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
 */
abstract class MyScoobiApp extends ScoobiApp {
  override def upload = true

  override def configureJars(implicit configuration: ScoobiConfiguration) = {
    uploadedJars.foreach(path =>
      DistributedCache.addFileToClassPath(new Path(path.toUri.getPath), configuration))

    // add new jars to the classpath and make sure that values are still unique for cache files and classpath entries
    configuration.addValues("mapred.classpath", jars.map(j => libjarsDirectory + (new File(j.getFile).getName)), ":")
  }
}
