/*
 * This file is part of CoAnSys project.
 * Copyright (c) 2012-2013 ICM-UW
 * 
 * CoAnSys is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.

 * CoAnSys is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with CoAnSys. If not, see <http://www.gnu.org/licenses/>.
 */

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
    val added =
      Option(DistributedCache.getFileClassPaths(configuration)).map(_.toList).getOrElse(List())
        .map(_.toUri.getPath).toSet
    uploadedJars
      .filterNot(path => added.contains(path.toUri.getPath))
      .foreach(path => DistributedCache.addFileToClassPath(new Path(path.toUri.getPath), configuration))

    // add new jars to the classpath and make sure that values are still unique for cache files and classpath entries
    configuration.addValues("mapred.classpath", jars.map(j => libjarsDirectory + (new File(j.getFile).getName)), ":")
  }
}
