CoAnSys/importers
=============

## About
CoAnSys/document-similarity is a subproject containing methods to easily find a set of similar documents or compute similarity between pairs of documents.

## Prerequirements
This package depends on [Hadoop](http://hadoop.apache.org/) from [Cloudera distribution](https://ccp.cloudera.com/display/SUPPORT/CDH+Downloads), version CDH 4.0.1 and appropriate [HBase](hbase.apache.org) version. 
As CDH4 depends on [Protocol Buffers](http://code.google.com/p/protobuf/) 2.4.1 so is CoAnSys/importers.

## Quick Start

### Instalation

### Example data acquisition

### Run sample application
#### a couple of bash commands

Please pass "-x local" option to run an example in a standalone mode, or ommit this option (or equivalently pass "-x mapreduce") to run in (pseudo)distributed mode.

* Calculate TF-IDF measure for each of the document based on terms from title, abstract and keywords list

```
$ cd CoAnSys/document-similarity/src/main/pig
$ pig -x local -p tableName=grotoap10 -p outputPath=tfidf tfidf.pig
# four subdirectories in output directory should be created
# three of them contain tf-idf values based on terms for abstract/keywords/title, and one directory that contains averaged results
$ ls tfidf/
abstract/ keyword/  title/    weighted/
```

* Calculate document similarity between documents based on a) average function, b) cosine function

```
# a) (compares documents that have at least one common term)
$ pig -x local -p tfidfPath=tfidf/weighted -p outputPath=pairwise_similarity pairwise_similarity.pig

# b) (compares all pairs of documents)
$ pig -x local -p tfidfPath=tfidf/weighted -p outputPath=pairwise_similarity allpairs_similarity.pig
```

#### an Oozie workflow

The same scripts can be run by an Oozie.

```
$ CoAnSys/document-similarity/src/main/oozie
# you may want to edit "similarity/local.properties" before running the workflow
$ ./submit-to-oozie.sh similarity 1 akawa localhost similarity/local.properties
```
