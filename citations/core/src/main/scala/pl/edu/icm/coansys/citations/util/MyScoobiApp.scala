package pl.edu.icm.coansys.citations.util

import com.nicta.scoobi.application.ScoobiApp
import com.nicta.scoobi.core.ScoobiConfiguration
import org.apache.hadoop.filecache.DistributedCache
import org.apache.hadoop.fs.Path

/**
 * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
 */
abstract class MyScoobiApp extends ScoobiApp {
  override def upload = true

  override def configureJars(implicit configuration: ScoobiConfiguration) = {
    super.configureJars
    uploadedJars.foreach(path =>
      DistributedCache.addFileToClassPath(new Path(path.toUri.getPath), configuration.configuration))
    configuration.configuration
  }
}
