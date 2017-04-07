package pl.edu.icm.coansys.document.deduplication
import scala.collection.JavaConversions._
import org.apache.spark.SparkContext
import org.apache.spark.SparkContext._
import java.util.function.BiPredicate
import org.apache.hadoop.io.BytesWritable
import org.apache.log4j.ConsoleAppender
import org.apache.log4j.Level
import org.apache.log4j.Logger
import org.apache.log4j.PatternLayout
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
import java.io.File
import java.io.PrintWriter
import pl.edu.icm.coansys.document.deduplication._
import scala.collection.JavaConverters._

object DeduplicateDocuments {
    val log = org.slf4j.LoggerFactory.getLogger(getClass().getName())
    val initialClusteringKeySize = 7
    val maximumClusteringKeySize = 18
    val maximumClusterSize = 600
    val tileSize = 50
    

    def isValidDocument(doc: DocumentWrapper): Boolean = {
        var res = false
        if (doc.hasDocumentMetadata) {
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
        //special case: if doc 
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
    implicit def toJavaBiPredicate[A, B](predicate: (A, B) ⇒ Boolean) =
        new BiPredicate[A, B] {
            def test(a: A, b: B) = predicate(a, b)
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
        val console = new ConsoleAppender(new PatternLayout("%d [%p|%c|%C{1}] %m%n"), ConsoleAppender.SYSTEM_OUT); //create appender
        console.activateOptions();
        Logger.getLogger("pl.edu.icm").addAppender(console)
        log.info("Reconfigured logger...")
        println("Created context...")
        sc.getConf.getAll.foreach(x => println(x._1 + ": " + x._2))
        val inputDocuments = args(0) //  "/user/kura/curr-res-navigator/hbase-sf-out/DOCUMENT"
        val outputDocuments = args(1)

        val rawbytes = sc.sequenceFile[String, BytesWritable](inputDocuments).mapValues(_.copyBytes)
        log.info("Loaded raw bytes.")

        val dirtyWrappers = rawbytes.mapValues(b => DocumentProtos.DocumentWrapper.parseFrom(b))

        //fix invalid documents:
        val fixedWrappers = if (fixInvalidDocuments) {
            val x = dirtyWrappers.filter(w => isValidDocument(w._2))
            val afterSize = x.count;
            val preSize = dirtyWrappers.count
            log.info("Filtering invalid documents done, before filtering: " + preSize + " and after filtering " + afterSize + " documents left.")
            x
        } else {
            dirtyWrappers
        }

        val wrappers = if (removeDoubles) {
            fixedWrappers.reduceByKey((x, y) => y)
        } else {
            fixedWrappers
        }
        wrappers.persist(StorageLevel.MEMORY_AND_DISK)
    
    
        //    val initialGroups = wrappers.map(t => (calculateKey(t._2.getDocumentMetadata, initialClusteringKeySize), t._2)).groupByKey()
        val initialGroups = prepareClustersV2(wrappers)
        initialGroups.persist(StorageLevel.MEMORY_AND_DISK)

        if (enableClusterSummary) {
            //prepare cluster summary  
            val largeClusters = initialGroups.filter(_._2.size > 1000)
            largeClusters.mapValues(_.size).take(250).foreach(println)
            val sampledResult = largeClusters.mapValues(x => (x.size, x.take(10))).collect
            //      val result = largeClusters.collect()
            sampledResult.foreach(x => {
                    val docs = x._2._2
                    val clusterId = x._1
                    val fileBase = clusterId + "-%04d-".format(docs.size)
                    println(fileBase)

                    println("Writing docs...")
                    var pw = new PrintWriter(new File(fileBase + "docs.txt"), "UTF-8")
                    docs.foreach(x => { pw.println(x.getDocumentMetadata()); pw.println("====================") })
                    pw.close
                })
            return
        }

        //    val igs = initialGroups.count()
        val clustersToDeduplicate = initialGroups.filter(t => t._2.size > 1)

        val tiledTasks = clustersToDeduplicate.flatMap(
            p => TileTask.parallelize(p._1, p._2, tileSize).map(v => (p._1, v))
        )

    

        val equalityClusters = tiledTasks.flatMapValues(
            v => { 
                val comparator = buildWorkComparator
                v.processPairs( (a: DocumentWrapper, b: DocumentWrapper) => 
                    comparator.isDuplicate(a.getDocumentMetadata, b.getDocumentMetadata, null))
                    }
                ).mapValues(v => List(v._1, v._2))
        val allItemsTraces = initialGroups.flatMapValues(l => l.map(v => List(v.getDocumentMetadata.getKey)))
        val finalClusters = equalityClusters.union(allItemsTraces).mapValues(List(_)).
            reduceByKey(
            (a, b) => TileTask.coalesceResult(a.map(x => x.asJava).asJava, b.map(x => x.asJava).asJava).
                asScala.toList.map(x => x.asScala.toList)
            ).flatMap(
                p => {
                    val cid = p._1
                    val cl = p._2
                    cl.zipWithIndex.map(q => (cid + f"_${q._2}%03d", q._1))
                })
        val finalDocClusters = finalClusters.flatMapValues(x => x).
            map(v => (v._2, v._1)).join(wrappers).map(v => (v._2._1, v._2._2)).
            groupByKey()
    
        val singularClustersAfterDeduplication = finalDocClusters.filter(p=>p._2.size==1)
        val pluralClustersAfterDeduplication = finalDocClusters.filter(p=>p._2.size>1)

        log.info("After reducing clusters (comparison)")
        val merged = pluralClustersAfterDeduplication.map(x => (mergeDocuments(x._2.toList)))
        log.info("Finished merge")
            //    val mergedSize = merged.count()

        val single = initialGroups.filter(t => t._2.size == 1)
            //    log.info("Got initial group count=" + igs + "; singular=" + single.count + 
            //             "; multiple=" + clustersToDeduplicate.count + "; reducedCount=" + deduplicatedClusters.count())
            //    
            //now merge the arrays:
        val toWrite = merged.union(single.union(singularClustersAfterDeduplication).map(x => x._2.head)).map(doc => (doc.getDocumentMetadata.getKey, doc))
        if ("-" != outputDocuments) {
            val bas = toWrite.mapValues(doc => doc.toByteArray())
            bas.saveAsSequenceFile(outputDocuments);
        } else {
            log.info("Simulating timing by counting.")
            toWrite.count()
        }
//    val tgrouped = timing.groupByKey;
//    val stats = tgrouped.map(x => timingStats(x._1, x._2)).collect
//    println("================ Timing stats ======================")
//    stats.sortBy(x => x._1).map(_.productIterator.toList.mkString(",")).foreach(println)
//    println("================ end of timing stats ======================")

        println("Exit")

    }
}
