package pl.edu.icm.coansys.citations.reducers

import scala.collection.JavaConversions.iterableAsScalaIterable
import scala.collection.mutable.ListBuffer

import org.apache.hadoop.io.Text
import org.apache.hadoop.mapreduce.Reducer

import pl.edu.icm.coansys.citations.data.MarkedText


/**
 * Reducer that takes documents and citations with the same
 * hash and generates every possible (citation, document) pair
 * for them.
 * Reducer will not generate any pair for hash if number
 * of possible pairs would exceed max.bucket.size value
 * (0 for no limit).
 * 
 * 
 * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
 * @author madryk
 */
class HashMatchingReducer extends Reducer[MarkedText, MarkedText, Text, Text] {
  type Context = Reducer[MarkedText, MarkedText, Text, Text]#Context

  val outKey = new Text()
  val outValue = new Text()
  
  val docs = new ListBuffer[String]
  val citations = new ListBuffer[String]

  var maxSize: Long = 0

  override def setup(context: Context) {
    maxSize = context.getConfiguration.getLong("max.bucket.size", 0)
  }
  
  override def reduce(key: MarkedText, values: java.lang.Iterable[MarkedText], context: Context) {
    
    docs.clear()
    citations.clear()
    
    for (value <- values) {
      if (value.isMarked.get()) {
        docs.append(value.text.toString)
      } else {
        citations.append(value.text.toString)
      }
    }
    val bucketSize = docs.size * citations.size
    
    if (bucketSize > 0 && (bucketSize <= maxSize || maxSize <= 0)) {
      
      for (citation <- citations) {
        for (doc <- docs) {
          outKey.set(citation)
          outValue.set(doc)
          context.write(outKey, outValue)
        }
      }
      
    }
  }
}
