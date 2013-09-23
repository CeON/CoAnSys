During the process of citation matching links from bibliography entries to referenced publications are created.

citations-main-workflow project contains the main citation matching workflow in oozie-maven-plugin format
(see further: <https://github.com/CeON/oozie-maven-plugin/wiki>).

Workflow properties you need to supply are listed in the workflow.xml file. Among them, the most important ones are:

* inputSeqFile - Hadoop SequenceFile containing documents in CoAnSys format as values,
DocumentWapper in /models/src/main/protobuf/bw2.proto
* outputSeqFile - Hadoop SequenceFile with matching results in CoAnSys format as values,
PicOut in /models/src/main/protobuf/pic_out.proto