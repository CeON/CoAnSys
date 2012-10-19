package pl.edu.icm.coansys.citations

import com.nicta.scoobi.core.Grouping

/**
 * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
 */
object StringGrouping extends Grouping[String] {
  override def partition(key: String, num: Int): Int = {
    val letters = 'a' to 'z'
    val charsNo = letters.length + 2
    def charMap(c: Char) =
      if (letters contains c)
        c.toInt - letters.start.toInt + 1
      else if (c < letters.start)
        0
      else
        letters.end.toInt - letters.start.toInt + 2
    def hash(s: String) =
      s.foldLeft(0L)((acc, c) => acc * charsNo + charMap(c))
    val substringLen = (math.log(num) / math.log(charsNo)).ceil.toInt
    val keysNo = math.pow(charsNo, substringLen).toInt
    val shardSize = (keysNo.toFloat / num).ceil.toInt
    (hash((key + (" " * math.max(0, substringLen - key.length))).substring(0, substringLen)) / shardSize).toInt
  }

  def groupCompare(x: String, y: String) = x.compareTo(y)
}
