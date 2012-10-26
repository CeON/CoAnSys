package pl.edu.icm.coansys.citations

import pl.edu.icm.coansys.commons.scala.strings.rotations
import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.{Path, FileSystem}
import java.net.URI
import org.apache.hadoop.io.{Writable, Text, MapFile}
import scala.util.control.Breaks.break
import collection.mutable.ListBuffer

/**
 * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
 */
class ApproximateIndex[V <: Writable : Manifest](val indexFileUri: String) {
  def get(key: String): Iterable[V] = {
    val conf = new Configuration()
    val rots = rotations(key + "$")
    val reader = new MapFile.Reader(new Path(indexFileUri), conf)
    val k: Text = new Text()
    var v: V = manifest[V].erasure.newInstance().asInstanceOf[V]
    val subrot: Text = new Text()

    rots.flatMap{rot =>
      println("Matching against " + rot)
      val buffer = new ListBuffer[V]
      var exit = false
      subrot.set(rot.substring(0, rot.length - 1))
      val kk = reader.getClosest(subrot, v, true)
      if (kk != null) {
        val current = kk.toString
        println("Found " + current)
        if ((current.startsWith(rot.substring(0, rot.length - 1)) && current.length <= rot.length) || (current.length == rot.length + 1 && current.startsWith(rot))) {
          println("Adding")
          buffer.append(v)
          v = manifest[V].erasure.newInstance().asInstanceOf[V]
        }
      }
      while(reader.next(k, v) && !exit) {
        val current = k.toString
        println("Testing " + current)
        if (!current.startsWith(rot.substring(0, rot.length - 1))) {
          println("Exiting")
          exit = true
        } else if (current.length <= rot.length || (current.length == rot.length + 1 && current.startsWith(rot))) {
          println("Got it!")
          buffer.append(v)
          v = manifest[V].erasure.newInstance().asInstanceOf[V]
        }
      }
      buffer
    }
  }

}
