/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */

package pl.edu.icm.coansys.citations.jobs

import com.nicta.scoobi.application.ScoobiApp
import org.apache.hadoop.fs.Path
import org.apache.hadoop.io.{Text, MapFile}
import pl.edu.icm.coansys.citations.util.BytesIterable

/**
 * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
 */
object IndexStatistics extends ScoobiApp {
  override def upload = false

  def run() {
    val indexFileUri = args(0)
    val reader = new MapFile.Reader(new Path(indexFileUri), configuration)
    val key = new Text()
    val value = new BytesIterable()
    println("Entries:")
    var noKey = 0
    var noVal = 0
    while (reader.next(key, value)) {
      print(key.toString)
      print("\t")
      println(value.iterable.size)
      noKey += 1
      noVal += value.iterable.size
    }
    println("All entries: " + noKey)
    println("All values: " + noVal)
  }
}
