package pl.edu.icm.coansys.document.deduplication
import scala.collection.JavaConversions._
import org.apache.spark.SparkContext
import org.apache.spark.SparkContext._
import java.util.function.BiPredicate
import org.apache.hadoop.io.BytesWritable
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
import org.apache.spark.storage.StorageLevel
import pl.edu.icm.coansys.deduplication.document.comparator.VotesProductComparator
import pl.edu.icm.coansys.deduplication.document.comparator.WorkComparator
import scala.collection.mutable.ListBuffer
import pl.edu.icm.coansys.document.deduplication._
import scala.collection.JavaConverters._

object DeduplicateDocuments {
  val log = org.slf4j.LoggerFactory.getLogger(getClass().getName())
  val initialClusteringKeySize = 7
  val maximumClusteringKeySize = 14
  val maximumClusterSize = 2000
  val tileSize = 100

    
    
  implicit def toJavaBiPredicate[A, B](predicate: (A, B) => Boolean) =
    new BiPredicate[A, B] {
      def test(a: A, b: B) = predicate(a, b)
    }

    
  def isValidDocument(doc: DocumentWrapper): Boolean = { //todo: fix based on if return value.
    var res = false;
    if (doc.hasDocumentMetadata()) {
      val md = doc.getDocumentMetadata
      if (md.hasBasicMetadata) {
        val bmd = md.getBasicMetadata
        if (bmd.getTitleCount() > 0 || bmd.getAuthorCount > 0 || bmd.hasDoi || bmd.hasJournal) {
          res = true
        }
      }
    }
    res
  }
    

  def calculateKey(doc: DocumentMetadata, size: Int): String = {
    new CustomOddsCharsKeyGenerator(size).generateKey(doc)
  }

  def calculateKeys(doc: DocumentMetadata): Array[String] = {
    val keySizes = initialClusteringKeySize to maximumClusteringKeySize
    val generator = new CustomOddsCharsKeyGenerator()
    generator.setKeySizes(keySizes.toArray)
    var res = generator.generateKeyList(doc)
    //special case: if doc has no title.
    if (res(0) == "") {
      res = Array.fill[String](keySizes.length)(doc.getKey)
    }
    res
  }

  def prepareClustersV2(inputDocs: RDD[(String, DocumentWrapper)]): RDD[(String, Iterable[DocumentWrapper])] = {
    log.info("Initializing cluster preparation (V2)")
    val keySizes = (initialClusteringKeySize to maximumClusteringKeySize).toArray
    log.info("Will use key sizes: " + keySizes.mkString(", "))
    val idClusterKeys = inputDocs.mapValues(doc => calculateKeys(doc.getDocumentMetadata())); //we loose documents here, ony ids are preseved
    val clusterDoc = idClusterKeys.flatMap(p => p._2.map(idcluster => (idcluster, p._1)))

    val clusterSizes = idClusterKeys.flatMap(x => (x._2.map(y => (y, 1)))).reduceByKey(_ + _)

    //build rdd (docId, (clusterId, clusterSize) )
    val docClustersWithSizes = clusterDoc.join(clusterSizes).map(p => (p._2._1, (p._1, p._2._2)))
    //build rdd - (docId, clusterId)
    val selectedClusters = docClustersWithSizes.reduceByKey((x, y) => {
      if (x._2 <= maximumClusterSize) {
        if (y._2 <= maximumClusterSize) {
          if (x._1.length <= y._1.length) { x } else { y }
        } else {
          x
        }
      } else {
        if (y._2 <= maximumClusterSize) {
          y
        } else {
          if (x._1.length > y._1.length) { x } else { y }
        }
      }
    }).mapValues(_._1)
    inputDocs.join(selectedClusters).map(p => (p._2._2, p._2._1)).groupByKey
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


  /**
   * @param args the command line arguments
   */
  def main(args: Array[String]): Unit = {
    val enableClusterSummary = false;
    val fixInvalidDocuments = true;
    val removeDoubles = true;

    println("Starting app")
    //load the file:
    if (args.size == 0) {
      println("No args supplied, exitting.")
      return ;
    } else { //todo: add arguments interpretation.
      println("Arguments:")
      args.foreach(println);
    }
    println("Creating context...")

    val conf = new SparkConf()
      .setAppName("Document deduplication")
      .set("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
      .set("spark.kryo.registrator", "pl.edu.icm.coansys.document.deduplication.DocumentWrapperKryoRegistrator")

    val sc = new SparkContext(conf)

    println("Created context...")
    sc.getConf.getAll.foreach(x => println(x._1 + ": " + x._2))
    val inputDocuments = args(0)
    //  "/user/kura/curr-res-navigator/hbase-sf-out/DOCUMENT"
    //  "/user/kura/curr-res-navigator-no-blogs/hbase-sf-out/DOCUMENT"
    val outputDocuments = args(1)

    val rawbytes = sc.sequenceFile[String, BytesWritable](inputDocuments).mapValues(_.copyBytes)
    println("Loaded raw bytes.")

    val dirtyWrappers = rawbytes.mapValues(b => DocumentProtos.DocumentWrapper.parseFrom(b))

    //fix invalid documents:
    val fixedWrappers = if (fixInvalidDocuments) {
      val x = dirtyWrappers.filter(w => isValidDocument(w._2))
      val afterSize = x.count;
      val preSize = dirtyWrappers.count
      log.info(f"Filtering invalid documents done, before filtering: $preSize and after filtering $afterSize documents left.")
      x
    } else {
      dirtyWrappers
    }

    val wrappers = if (removeDoubles) {
      fixedWrappers.reduceByKey((x, y) => y)
    } else {
      fixedWrappers
    }
//    wrappers.persist(StorageLevel.MEMORY_AND_DISK)

    val initialSize = wrappers.count
    println(f"Starting processing with $initialSize documents.")

    val initialGroups = prepareClustersV2(wrappers)
    initialGroups.persist//(StorageLevel.MEMORY_AND_DISK)

    val clustersToDeduplicate = initialGroups.filter(t => t._2.size > 1)
    val initialClusterCount = clustersToDeduplicate.count
    //TODO: some statistics here on cluster, would be useful.

    val tiledTasks = clustersToDeduplicate.flatMap(p => TileTask.parallelize(p._1, p._2, tileSize))
    tiledTasks.persist//(StorageLevel.MEMORY_AND_DISK)
    val tileCount = tiledTasks.count;

    println(f"Prepared $initialClusterCount clusters, and then split it to $tileCount tiles")
    //        val comparator = sc.broadcast(buildWorkComparator) //todo: can we do comparator broadcast variable?

    val partialEqualityClusters = tiledTasks.flatMap(
      v => {
        val t0 = java.lang.System.currentTimeMillis()
        //                    log.info("Starting tile task %s".format(v.getTaskId))
        val comparator = buildWorkComparator
        val res = v.processPairs((a: DocumentWrapper, b: DocumentWrapper) =>
          comparator.isDuplicate(a.getDocumentMetadata, b.getDocumentMetadata, null))
        val time = (java.lang.System.currentTimeMillis() - t0) / 1000.0
        log.info(f"Finishing tile task ${v.getTaskId} in $time%.4f sec")
        res
      }
    )
    // At this moment we have RDD of (String, Seq[String]) (clusterId and list of 'equal' ids inside this cluster    
    // let's save it: 
    // partialEqualityClusters.saveAsObjectFile ("hdfs:///user/axnow/intermediate/pec");
    // //later, in other program - watch, you have tuples out of the box :)
    // val loadedPec  = sc.objectFile[(String, java.util.List[String])]("intermediate/pec")
    // val partialEqualityClusters = loadedPec
    // 
    // 
    //        equalityClusters.persist(StorageLevel.MEMORY_AND_DISK)
    //        log.info("Got totally "+partialEqualityClusters.count+" partial equality clusters.");
    //        val filteredSizes = partialEqualityClusters.mapValues(x=>1).reduceByKey(_+_).filter(p=>p._2>500)
    //        log.info("After filtering "+partialEqualityClusters.count+" equality clusters left.");
    //        
    //        val eqclsizes= filteredSizes.collect.sortBy (_._2)
    //        println("Equality cluster sizes:")
    //        eqclsizes.foreach(println(_))
    //        println("Done.\n\n")

    val finalClusters = partialEqualityClusters.mapValues(List(_)).
      reduceByKey(_ ++ _). //one long list of lists of ids
      map(pair => {
        val t0 = java.lang.System.currentTimeMillis()
        val res = TileTask.coalesceResult(pair._2.asJava)
        val tt = System.currentTimeMillis() - t0;
        val clusterSize=pair._2.size
        log.info(f"Finished tile coalesce task. (Cluster,time[s], size): ${pair._1}, ${tt / 1000.0}, ${pair._2.size}")
        (pair._1, res)
      }).
      flatMap( //build proper ids for equality clusters.
        p => {
          val cid = p._1
          val cl = p._2
          cl.zipWithIndex.map(q => (cid + f"_${q._2}%03d", q._1))
        }
      )
    //now we got all the items in place    
    finalClusters.persist(StorageLevel.MEMORY_AND_DISK_SER);
    val finclSizes = finalClusters.mapValues(_.size).takeOrdered(100)(Ordering[Int].on(-_._2))
    println("Top 100 cluster sizes:")
    finclSizes.foreach(println(_))
    println("-----\n\n")

    //count clusters, documents in clusters and number of documents to be deduced:
    val finalClusterCount = finalClusters.count
    val documentInFinalClusterCount = finalClusters.map(_._2.size).reduce(_ + _)
    val documentRemovedDuringClusteringCount = documentInFinalClusterCount - finalClusterCount
    println(f"Finally created $finalClusterCount clusters, containing $documentInFinalClusterCount documents, $documentRemovedDuringClusteringCount documents will be removed.")

    // merge documents
    val docIdWithClusterId = finalClusters.flatMapValues(x => x).
      map(v => (v._2, v._1))
    val documentWrappersPrepared = wrappers.leftOuterJoin(docIdWithClusterId);
    val mergedDocuments = documentWrappersPrepared.filter(_._2._2.isDefined).
      map(x => (x._2._2, List(x._2._1))).reduceByKey(_ ++ _). //get lists of cluster documents by cluster id
      map(kv => {
        val doc = mergeDocuments(kv._2)
        (doc.getDocumentMetadata.getKey, doc)
      })

    // documents not touched
    val singularDocuments = documentWrappersPrepared.filter(_._2._2.isEmpty).map(x => (x._1, x._2._1))

    //final result.
    val finalResult = singularDocuments.union(mergedDocuments)
    finalResult.persist
    
    val finalSize = finalResult.count
    println(f"Final counts:\n-----------\n"+
            f" input: $initialSize\n"+
            f" output: $finalSize\n"+
            f" removed: $documentRemovedDuringClusteringCount\n"+
            f" difference: ${initialSize-finalSize-documentRemovedDuringClusteringCount}")
        
    if ("-" != outputDocuments) {
      val bas = finalResult.mapValues(doc => doc.toByteArray()).saveAsSequenceFile(outputDocuments);
    } else {
      log.info("Simulating timing by counting.")
      finalResult.count()
      println("Finished counting.")
    }
  }
}
