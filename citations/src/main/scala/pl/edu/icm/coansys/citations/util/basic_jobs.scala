/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */

package pl.edu.icm.coansys.citations.util

import com.nicta.scoobi.Scoobi._
import org.apache.hadoop.fs.{Path, FileSystem}

/**
 * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
 */
object basic_jobs {
  def sort[K, V](indexFile: String, tmpFile: String = "")
                (implicit conf: ScoobiConfiguration,
                 evidence$1: Manifest[K],
                 evidence$2: WireFormat[K],
                 evidence$3: SeqSchema[K],
                 evidence$4: Manifest[V],
                 evidence$5: WireFormat[V],
                 evidence$6: SeqSchema[V]) {
    val fs = FileSystem.get(conf)
    val tmp = if (!tmpFile.isEmpty) tmpFile else indexFile + "_tmp"
    fs.rename(new Path(indexFile), new Path(tmp))
    val maxRed = conf.getMaxReducers
    conf.setMaxReducers(1)
    val entities = convertFromSequenceFile[K, V](tmpFile)
    persist(convertToSequenceFile[K, V](entities.groupBy(_ => 0).values.flatten, indexFile))
    conf.setMaxReducers(maxRed)
  }
}
