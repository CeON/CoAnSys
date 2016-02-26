Citation Matching Module
========================

This CoAnSys module performs citation matching. During this process links from bibliography entries to referenced publications are created.

citations-full-workflow project contains the main citation matching workflow in oozie-maven-plugin format
(see further: <https://github.com/CeON/oozie-maven-plugin/wiki>).

## Configuration ##

Workflow properties you need to supply are listed in the workflow.xml file. Among them, the most important ones are:

* input - Hadoop SequenceFile containing documents in CoAnSys format as values,
DocumentWapper in /models/src/main/protobuf/bw2.proto
* output - Hadoop SequenceFile with matching results in CoAnSys format as values,
PicOut in /models/src/main/protobuf/pic_out.proto

## Exemplary job.properties file ##

    masterNode=hadoop-master.example.com
    nameNode=hdfs://${masterNode}:8020
    jobTracker=${masterNode}:8021
    oozie.wf.application.path=${nameNode}/user/myuser/citations-full-workflow
    workingDir=${nameNode}/user/myuser/citations-full-working
    input=mydata.sf
    output=${workingDir}/output

## Running ##

    $ mvn clean install -DjobPackage
    $ hadoop fs -put ./citations-full-workflow/target/oozie-wf/ citations-full-workflow
    $ oozie job -oozie http://hadoop-master:11000/oozie -config job.properties -run


## Publications ##
* M. Fedoryszak and Ł. Bolikowski,
  “Efficient blocking method for a large scale citation matching,”
  *to appear in WOSP 2014 proceedings*.
* M. Fedoryszak, D. Tkaczyk, and Ł. Bolikowski,
  “[Large Scale Citation Matching Using Apache Hadoop](http://link.springer.com/chapter/10.1007/978-3-642-40501-3_37),”
   in _Research and Advanced Technology for Digital Libraries_, 
   T. Aalberg, C. Papatheodorou, M. Dobreva, G. Tsakonas, and C. J. Farrugia (Eds.),
   Springer, 2013, pp. 362–365.
* M. Fedoryszak, Ł. Bolikowski, D. Tkaczyk, and K. Wojciechowski,
  “[Methodology for evaluating citation parsing and matching]
   (http://depot.ceon.pl/bitstream/handle/123456789/1964/synat2012_submission_6.pdf?sequence=1),”
   in _Intelligent Tools for Building a Scientific Information Platform_,
   R. Bembenik, L. Skonieczny, H. Rybinski, M. Kryszkiewicz, and M. Niezgódka (Eds.),
   Springer, 2013, pp. 145–154.
