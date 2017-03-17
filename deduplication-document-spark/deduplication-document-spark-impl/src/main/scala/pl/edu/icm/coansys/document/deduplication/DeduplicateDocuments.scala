package pl.edu.icm.coansys.document.deduplication
import scala.collection.JavaConversions._
import org.apache.spark.SparkContext
import org.apache.spark.SparkContext._
import org.apache.hadoop.io.BytesWritable
import org.apache.log4j.Level
import org.apache.log4j.Logger
import org.apache.spark.SparkConf
import pl.edu.icm.coansys.deduplication.document.voter.AuthorsVoter
import pl.edu.icm.coansys.deduplication.document.voter.DoiVoter
import pl.edu.icm.coansys.deduplication.document.voter.IssueVolumeVoter
import pl.edu.icm.coansys.deduplication.document.voter.JournalVoter
import pl.edu.icm.coansys.deduplication.document.voter.PagesVoter
import pl.edu.icm.coansys.deduplication.document.voter.SimilarityVoter
import pl.edu.icm.coansys.deduplication.document.voter.TitleVoter
import pl.edu.icm.coansys.deduplication.document.voter.YearVoter
import pl.edu.icm.coansys.document.deduplication.merge.AdvancedDuplicatesMerger
import pl.edu.icm.coansys.document.deduplication.merge.DuplicatesMerger
import pl.edu.icm.coansys.models.DocumentProtos
import pl.edu.icm.coansys.models.DocumentProtos._
import org.apache.spark.rdd.RDD
import pl.edu.icm.coansys.deduplication.document.comparator.VotesProductComparator
import pl.edu.icm.coansys.deduplication.document.comparator.WorkComparator
import scala.collection.mutable.ListBuffer

object DeduplicateDocuments {
  val log = org.slf4j.LoggerFactory.getLogger(getClass().getName())
  val initialClusteringKeySize = 5
  val maximumClusteringKeySize = 15
  val maximumClusterSize = 300
  //max author count - ignored

  //  def calculateKey(doc: DocumentMetadata): String = {
  //    new OddsCharsKeyGenerator().generateKey(doc)
  //  }

  def calculateKey(doc: DocumentMetadata, size: Int): String = {
    new CustomOddsCharsKeyGenerator(size).generateKey(doc)
  }

  def prepareClusters(inputDocs: RDD[DocumentWrapper]): RDD[(String, Iterable[DocumentWrapper])] = {
    log.info("Started preparation of clusters.")
    var keySize = initialClusteringKeySize

    var approvedClusters: RDD[(String, Iterable[DocumentWrapper])] = null
    var docs = inputDocs
    var docCount = 0L
    var iteration = 1

    while ({ docCount = docs.count; docCount } > 0 && keySize < maximumClusteringKeySize) {
      log.info("Starting iteration %d, keySize: %d, docs to count: %d".format(iteration, keySize, docCount))

      var processedClusters = docs.map(doc => (calculateKey(doc.getDocumentMetadata, keySize), doc)).groupByKey()
      var posClusters = processedClusters.filter(p => p._2.size <= maximumClusterSize)
      if (approvedClusters == null) {
        approvedClusters = posClusters
      } else {
        approvedClusters = approvedClusters.union(posClusters)
      }
      docs = processedClusters.filter(p => p._2.size > maximumClusterSize).flatMap(p => p._2)
      keySize += 1
      iteration += 1
    }
    log.info("Finished loop, keySize: %d, iterations done: %d, docs left: %d".format(keySize - 1, iteration - 1, docCount))
    if (docCount > 0) {
      log.info("Adding leftovers (%d documents) with keySize %d".format(docCount, keySize))
      approvedClusters = approvedClusters.union(docs.map(doc => (calculateKey(doc.getDocumentMetadata, keySize), doc)).groupByKey())
    }
    approvedClusters
  }

  /**
   * @param args the command line arguments
   */
  def main(args: Array[String]): Unit = {
    println("Starting app")
    //load the file:
    if (args.size == 0) {
      println("No args supported, exitting.")
      return ;
    } else {
      println("Arguments:")
      args.foreach(println);
    }
    println("Creating context...")

    val conf = new SparkConf()
      .setAppName("Document deduplication")
      .set("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
      .set("spark.kryo.registrator", "pl.edu.icm.coansys.document.deduplication.DocumentWrapperKryoRegistrator")

    val sc = new SparkContext(conf)
    Logger.getLogger("pl.edu.icm.coansys.deduplication.document.comparator.AbstractWorkComparator").setLevel(Level.WARN)
    println("Created context...")
    sc.getConf.getAll.foreach(x => println(x._1 + ": " + x._2))
    val inputDocuments = args(0)
    val outputDocuments = args(1)

    //    val debugDir = if (args.size > 2) Some(args(2)) else None

    val rawbytes = sc.sequenceFile[String, BytesWritable](inputDocuments).mapValues(_.copyBytes)
    log.info("Loaded raw bytes.")
    //    rawbytes.count()
    //    log.info("Counted raw bytes.")

    val wrappers = rawbytes.mapValues(b => DocumentProtos.DocumentWrapper.parseFrom(b))

    
//    val initialGroups = wrappers.map(t => (calculateKey(t._2.getDocumentMetadata, initialClusteringKeySize), t._2)).groupByKey()
    val initialGroups = prepareClusters(wrappers.map(_._2))
    log.info("After initial group preparation count.")
    //    val igs = initialGroups.count()
    val clustersToDeduplicate = initialGroups.filter(t => t._2.size > 1)
    //    val deduplicatedClusters = clustersToDeduplicate.flatMap(x => deduplicateCluster(x._2, x._1))
    val timedDeduplicated = clustersToDeduplicate.map(x => timedDeduplicateCluster(x._2, x._1))
    val deduplicatedClusters = timedDeduplicated.flatMap(x => x._1)
    val timing = timedDeduplicated.map(x => x._2)

    log.info("After reducing clusters (comparison)")
    val merged = deduplicatedClusters.map(x => (mergeDocuments(x._2)))
    log.info("Finished merge")
    //    val mergedSize = merged.count()

    val single = initialGroups.filter(t => t._2.size == 1)
    //    log.info("Got initial group count=" + igs + "; singular=" + single.count + 
    //             "; multiple=" + clustersToDeduplicate.count + "; reducedCount=" + deduplicatedClusters.count())
    //    
    //now merge the arrays:
    val toWrite = merged.union(single.map(x => x._2.head)).map(doc => (doc.getDocumentMetadata.getKey, doc))
    if ("-" != outputDocuments) {
      val bas = toWrite.mapValues(doc => doc.toByteArray())
      bas.saveAsSequenceFile(outputDocuments);
    }
    val tgrouped = timing.groupByKey;
    val stats = tgrouped.map(x => timingStats(x._1, x._2)).collect
    println("================ Timing stats ======================")
    stats.sortBy(x => x._1).map(_.productIterator.toList.mkString(",")).foreach(println)
    println("================ end of timing stats ======================")

    println("Exit")

  }

  def buildDocumentsMerger(): DuplicatesMerger = {
    val res = new AdvancedDuplicatesMerger
    res.setup("")
    res
  }

  def mergeDocuments(docs: List[DocumentWrapper]): DocumentWrapper = {
    val merger = buildDocumentsMerger()
    val merged = merger.merge(docs);
    merged
  }

  def buildWorkComparator(): WorkComparator = {
    val result = new VotesProductComparator;
    result.setMinVotersWeightRequired(1.5f)
    result.setProbabilityTreshold(0.5f)
    result.setTresholdIncreasingVotersRequired(0.7f)

    val voters = new ListBuffer[SimilarityVoter]()
    val dv = new DoiVoter()
    dv.setWeight(1.0f)
    voters += dv
    val jv = new JournalVoter()
    jv.setWeight(0.3f)
    jv.setDisapproveLevel(0.5f)
    jv.setApproveLevel(0.05f)
    voters += jv

    val wivv = new IssueVolumeVoter
    wivv.setWeight(0.3f)
    wivv.setAbstainIfAbsent(true)
    wivv.setSubsetResult(0.8f)
    wivv.setPartiallyMatchResult(0.52f)
    voters += wivv

    val wpv = new PagesVoter
    wpv.setWeight(.3f)
    wpv.setAbstainIfAbsent(true)
    wpv.setAbsentResult(0.6f)
    wpv.setSubsetResult(0.75f)
    wpv.setPartiallyMatchResult(0.64f)
    wpv.setRemoveRepeated(true)
    voters += wpv

    val wyv = new YearVoter
    wyv.setWeight(.3f)
    wyv.setAbstainIfAbsent(true)
    wyv.setAbsentResult(.52f)
    wyv.setSubsetResult(.9f)
    wyv.setPartiallyMatchResult(.75f)
    wyv.setRemoveRepeated(true)
    voters += wyv

    val wtv = new TitleVoter()
    wtv.setWeight(0.8f)
    wtv.setDisapproveLevel(0.11f)
    wtv.setApproveLevel(0.001f)
    wtv.setMaxNormalizedTitleLength(90)
    voters += wtv

    val wav = new AuthorsVoter
    wav.setWeight(0.8f)
    wav.setDisapproveLevel(0.2f)
    wav.setApproveLevel(0.03f)
    voters += wav

    result.setSimilarityVoters(voters)
    result;
  }

  //size, num, min time, max time, avg time, avg_group_count
  def timingStats(size: Int, measurements: Iterable[(Int, Long)]): (Int, Int, Double, Double, Double, Double) = {
    val count = measurements.size
    val times = measurements.map(_._2)
    val minTime = times.min / 1000.0
    val maxTime = times.max / 1000.0
    val avgTime = times.sum / (1000.0 * count)
    val avgGroupSize = measurements.map(_._1).sum / (1.0 * count)
    (size, count, minTime, maxTime, avgTime, avgGroupSize);
  }

  def clusterAnalysis(cluster: Iterable[DocumentWrapper], clusterId: String): (List[DocumentWrapper], Array[Array[Int]]) = {
    log.info("Analysing cluster {}, size: {}", clusterId, cluster.size)

    val res = Array.ofDim[Int](cluster.size, cluster.size)
    val sorted = cluster.toList.sortBy(dw => dw.getDocumentMetadata.getKey)
    val comparator = buildWorkComparator()

    val sind = sorted.toList.zipWithIndex

    val procStart = System.currentTimeMillis
    sind.foreach(p1 => {
      val i1 = p1._2
      val d1 = p1._1
      //make sure diagonal is zeroed
      (0 to cluster.size - 1).foreach(i => res(i)(i) = 0)
      //simple iteration over table
      sind.foreach(p2 => {
        val i2 = p2._2
        val d2 = p2._1
        if (i1 < i2) {
          val s = System.currentTimeMillis
          comparator.isDuplicate(d1.getDocumentMetadata, d2.getDocumentMetadata, null)
          val e = System.currentTimeMillis
          res(i1)(i2) = (e - s).toInt
        }
      })
    })
    val procEnd = System.currentTimeMillis
    val elapsedSteps = res.map(_.sum).sum
    log.info("Finished processing, elapsed: %.3f, sum of steps: %.3f".format((procEnd - procStart) / 1000.0, elapsedSteps / 1000.0))

    (sorted, res)
  }

  //todo: timing depending on size
  //Deduplicates the documents within single cluster, and creates a number of clusters containing documensts which
  //are assumed to be duplicates.
  def deduplicateCluster(cluster: Iterable[DocumentWrapper], clusterId: String): Iterable[(String, List[DocumentWrapper])] = {
    log.info("Deduplicating cluster {}, size: {}", clusterId, cluster.size)
    //
    Logger.getLogger("pl.edu.icm.coansys.deduplication.document.comparator.AbstractWorkComparator").setLevel(Level.WARN)
    if (cluster.size > 30) {

      log.info("Sample document: \n{}", cluster.head.getDocumentMetadata.getBasicMetadata)
    }
    val startTime = System.currentTimeMillis();
    val classes = ListBuffer[ListBuffer[DocumentWrapper]]()
    val comparator = buildWorkComparator()
    cluster.foreach(x => {
      val arr = classes.find(a => { comparator.isDuplicate(x.getDocumentMetadata, a(0).getDocumentMetadata, null) })
      if (arr.isEmpty) {
        val nclass = ListBuffer(x)
        classes += nclass
      } else {
        arr.get += x
      }
    })
    val t: Double = (System.currentTimeMillis() - startTime) / 1000.0
    val res = classes.zipWithIndex.map(x => (clusterId + x._2, x._1.toList))
    log.info("Deduplicated cluster " + clusterId + ", size " + cluster.size + " in " + t + " sec into " + res.size + " groups.")

    res

  }

  def timedDeduplicateCluster(cluster: Iterable[DocumentWrapper], clusterId: String): (Iterable[(String, List[DocumentWrapper])], (Int, (Int, Long))) = {
    val start = System.currentTimeMillis;
    val rresult = deduplicateCluster(cluster, clusterId);
    val end = System.currentTimeMillis

    (rresult, (cluster.size, (rresult.size, end - start)))
  }

}
