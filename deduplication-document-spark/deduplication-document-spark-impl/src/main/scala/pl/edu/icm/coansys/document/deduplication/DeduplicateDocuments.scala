/*
 * This file is part of CoAnSys project.
 * Copyright (c) 2012-2017 ICM-UW
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
import pl.edu.icm.coansys.deduplication.document.comparator.VotesProductComparator
import pl.edu.icm.coansys.deduplication.document.comparator.WorkComparator
import scala.collection.mutable.ListBuffer
import pl.edu.icm.coansys.document.deduplication._
import scala.collection.JavaConverters._

/** Main application for the deduplication of the documents. 
 * 
 */
object DeduplicateDocuments {
  val log = org.slf4j.LoggerFactory.getLogger(getClass().getName())

  implicit def toJavaBiPredicate[A, B](predicate: (A, B) => Boolean) =
    new BiPredicate[A, B] {
      def test(a: A, b: B) = predicate(a, b)
    }

  def isValidDocument(doc: DocumentWrapper): Boolean = { //todo: fix based on if return value.
    if (doc.hasDocumentMetadata()) {
      val md = doc.getDocumentMetadata
      if (md.hasBasicMetadata) {
        val bmd = md.getBasicMetadata
        (bmd.getTitleCount() > 0 || bmd.getAuthorCount > 0 || bmd.hasDoi || bmd.hasJournal)
      } else {
          false
      }
    } else {
        false
    }
  }

    
  def calculateKeys(doc: DocumentMetadata, initialClusteringKeySize: Int, maximumClusteringKeySize: Int): Seq[String] = {
    val keySizes = initialClusteringKeySize to maximumClusteringKeySize
    var res = MultiLengthTitleKeyGenerator.generateKeys(doc)(keySizes)
    if (res.head.isEmpty) {
      res = Array.fill[String](keySizes.length)(doc.getKey)
    }
    res
  }

  /**
   * Group items into large clusters, within which detailed analysis will be
   * held. 
   *
   *  Items are grouped by keys generated from the normalised titles.
   * If the cluster is too big, then longer keys are used, so smaller clusters are 
   * generated. Treshold is maximumClusterSize.
   * 
   */
  def prepareInitialClustering(inputDocs: RDD[(String, DocumentWrapper)], initialClusteringKeySize: Int,
    maximumClusteringKeySize: Int, maximumClusterSize: Int): RDD[(String, Iterable[DocumentWrapper])] = {
    log.info("Initializing cluster preparation.")
    val keySizes = initialClusteringKeySize to maximumClusteringKeySize
    log.info("Will use key sizes: " + keySizes.mkString(", "))
    
    val idClusterKeys = inputDocs.mapValues(doc => calculateKeys(
            doc.getDocumentMetadata(), initialClusteringKeySize, maximumClusteringKeySize)) //we loose documents here, ony ids are preseved
    val clusterDoc = idClusterKeys.flatMap(kv => kv._2.map(idcluster => (idcluster, kv._1))) // (clusterId => docId)
    val clusterSizes = idClusterKeys.flatMap(x => (x._2.map(y => (y, 1)))).reduceByKey(_ + _) //(clusterId => clusterSize)
    
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

  /**
   * Merge the documents using appropriate document merger.
   */
  def mergeDocuments(docs: List[DocumentWrapper]): DocumentWrapper = {
    val merger = buildDocumentsMerger()
    val merged = merger.merge(docs);
    merged
  }

  /**
   * Defines comparator according to the weights resulting from experiments. 
   * 
   * This is reimplementation of the original Spring XML bean definition, which 
   * was unnecessary complication at this moment.
   */
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

    
  case class Config(
    inputFile: String = "",
    outputFile: String = "",
    dumpClusters: Boolean = false,
    keySizeMin: Int = 5,
    keySizeMax: Int = 15,
    clusterSizeMax: Int = 500,
    tileSize: Int = 25,
    filterInvalidDocuments: Boolean = false,
    removeDuplicateDocuments: Boolean = false
  )

  /** Load the documents from the given sequence file, do the optional 
   * cleanups.
   * 
   */
  def loadDocuments( sc: SparkContext, file: String,
                    filterInvalid: Boolean, removeDoubles: Boolean):RDD[(String, DocumentWrapper)] = {
         val rawbytes = sc.sequenceFile[String, BytesWritable](file).mapValues(_.copyBytes)
    println("Loaded raw bytes.")

    val dirtyWrappers = rawbytes.mapValues(b => DocumentProtos.DocumentWrapper.parseFrom(b))

    //fix invalid documents:
    val fixedWrappers = if (filterInvalid) {
      val x = dirtyWrappers.filter(w => isValidDocument(w._2))
      val afterSize = x.count;
      val preSize = dirtyWrappers.count
      log.info(f"Filtering invalid documents done, before filtering: $preSize and after filtering $afterSize documents left.")
      x
    } else {
      dirtyWrappers
    }

    if (removeDoubles) {
      fixedWrappers.reduceByKey((x, y) => y)
    } else {
      fixedWrappers
    }
  }
  
  /** Debug method to printout top clusters. */
  def printTopClusters(finalClusters:RDD[(String, Seq[String])], count:Int):Unit = {
    val finclSizes = finalClusters.mapValues(_.size).takeOrdered(100)(Ordering[Int].on(-_._2))
    println("Top 100 cluster sizes:")
    finclSizes.foreach(println(_))
    println("-----\n\n")

  }
  
    
  
     
  def main(args: Array[String]): Unit = {
    val fixInvalidDocuments = true;
    val removeDoubles = true;

    println("Starting document deduplication")

    val parser = new scopt.OptionParser[Config]("CoAnSys Deduplicate Documents") {
      head("Deduplicate documents", "0.1")
                
      opt[Unit]('f', "filter-invalid").action((x, c) =>
        c.copy(filterInvalidDocuments = true)).text("filter invalid (empty) documents before run.")
                
      opt[Unit]('d', "remove-doubles").action((x, c) =>
        c.copy(removeDuplicateDocuments = true)).text("filter out duplicates sharing the same key before processing.")
                
      opt[Int]("cluster-key-min").abbr("kmn").action((x, c) => c.copy(keySizeMin = x)).
        validate(x =>
          if (x >= 2) success
          else failure("Value <cluster-key-min> must be >=2")).
        text("shortest valid key for cluster, defines pre-clustering. Recommended value more thab 4, minimum 2.")

      opt[Int]("cluster-key-max").abbr("kmx").action((x, c) => c.copy(keySizeMax = x)).
        validate(x =>
          if (x >= 2 && x <= 20) success
          else failure("Value <cluster-key-min> must be >=2")).
        text("longest valid key for cluster, during pre-clustering. Used to split large clusters. Recommended value more than min, minimum 2, max 20.")

      opt[Int]("cluster-size-max").abbr("cs").action((x, c) => c.copy(clusterSizeMax = x)).
        text("Largest acceptable cluster size during preclustering phase. If cluster exceeds algorithm attempts to use longer key if possible. Typically 400+")

      opt[Int]("tile-size").abbr("ts").action((x, c) => c.copy(keySizeMax = x)).
        validate(x =>
          if (x >= 2) success
          else failure("Value <tile-size> must be >=2")).
        text("Size of the tile tasks used to split large clusters. Min 2, recommended approx 40")

      arg[String]("<input>").required.text("Input sequence file").action((f, c) => c.copy(inputFile = f))
      arg[String]("<output>").optional.text("Output sequence file. If ommited, then no output is written but calculation is done.").action((f, c) => c.copy(outputFile = f))
      note("Blah")
    }

    val cfg: Config = parser.parse(args, Config()) match {
      case Some(config) =>
        println(f"Got config:\n${config}")
        println(config);
        config
      case None =>
        // arguments are bad, error message will have been displayed
        println("No config, aborting.")
        return
    }

    println("Creating context...")

    //required to operate protobuf correctly
    val conf = new SparkConf()
      .setAppName("Document deduplication")
      .set("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
      .set("spark.kryo.registrator", "pl.edu.icm.coansys.document.deduplication.DocumentWrapperKryoRegistrator")

    val sc = new SparkContext(conf)

    println("Created context...")
    //    sc.getConf.getAll.foreach(x => println(x._1 + ": " + x._2))
    val inputDocuments = cfg.inputFile
    // for pasting into console:
    //  val inputDocuments = "/user/kura/curr-res-navigator/hbase-sf-out/DOCUMENT"
    //  val inputDocuments = "/user/kura/curr-res-navigator-no-blogs/hbase-sf-out/DOCUMENT"
    val outputDocuments = cfg.outputFile
    
        
    //load documents 
    val wrappers = loadDocuments(sc, inputDocuments, cfg.filterInvalidDocuments, cfg.removeDuplicateDocuments)
    val initialSize = wrappers.count
    println(f"Starting processing with $initialSize documents.")

    val initialGroups = prepareInitialClustering(wrappers, cfg.keySizeMin, cfg.keySizeMax, cfg.clusterSizeMax)
    initialGroups.persist

    val clustersToDeduplicate = initialGroups.filter(t => t._2.size > 1)
    val initialClusterCount = clustersToDeduplicate.count
    //TODO: some statistics here on cluster, would be useful.

    val tiledTasks = clustersToDeduplicate.flatMap(p => CartesianTaskSplit.parallelizeCluster(p._1, p._2, cfg.tileSize))
    
    tiledTasks.persist
    val tileCount = tiledTasks.count;

    println(f"Prepared $initialClusterCount clusters, and then split it to $tileCount tiles")
    
    //build (clusterId, Seq(docId)) rdd:    
    val partialEqualityClusters = tiledTasks.flatMap(
      task => {
        val t0 = java.lang.System.currentTimeMillis()
        val comparator = buildWorkComparator
        val res = task.processPairs((a: DocumentWrapper, b: DocumentWrapper) =>
          comparator.isDuplicate(a.getDocumentMetadata, b.getDocumentMetadata, null))
        val time = (java.lang.System.currentTimeMillis() - t0) / 1000.0
        //useful for identification of possible problem.
        log.info(f"Finishing tile task ${task.taskId} in $time%.4f sec")
        res.map((task.clusterId, _))
      }
    )
    
    val finalClusters = partialEqualityClusters.mapValues(List(_)).
      reduceByKey(_ ++ _). //one long list of lists of ids for each cluster
      map(pair => {
        val t0 = java.lang.System.currentTimeMillis()
        val res = CartesianTaskSplit.coalesceClusters(pair._2.asJava)
        val tt = System.currentTimeMillis() - t0;
        val clusterSize = pair._2.size
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
    finalClusters.persist
    
    printTopClusters(finalClusters, 100)

    //count clusters, documents in clusters and number of documents to be deduced:
    val finalClusterCount = finalClusters.count
    val documentInFinalClusterCount = finalClusters.map(_._2.size).fold(0)(_ + _)
    val documentRemovedDuringClusteringCount = documentInFinalClusterCount - finalClusterCount
    println(f"Finally created $finalClusterCount clusters, containing $documentInFinalClusterCount documents, $documentRemovedDuringClusteringCount documents will be removed.")

    // merge documents
    val docIdWithClusterId = finalClusters.flatMapValues(x => x).
      map(v => (v._2, v._1))
    val documentWrappersPrepared = wrappers.leftOuterJoin(docIdWithClusterId);
    val mergedDocuments = documentWrappersPrepared.filter(_._2._2.isDefined).
      map(x => (x._2._2, List(x._2._1))).foldByKey(List())(_ ++ _). //get lists of cluster documents by cluster id
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
    println(f"Final counts:\n-----------\n" +
      f" input: $initialSize\n" +
      f" output: $finalSize\n" +
      f" removed: $documentRemovedDuringClusteringCount\n" +
      f" difference: ${initialSize - finalSize - documentRemovedDuringClusteringCount}")

    if ("-" != outputDocuments && !outputDocuments.isEmpty) {
      val bas = finalResult.mapValues(doc => doc.toByteArray()).saveAsSequenceFile(outputDocuments);
    } else {
      log.info("Simulating timing by counting.")
      finalResult.count()
      println("Finished counting.")
    }
  }
}
