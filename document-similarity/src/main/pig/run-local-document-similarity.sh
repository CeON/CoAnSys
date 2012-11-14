#!/bin/bash

OUTPUT_PATH=$1
JARS=../../../../integration/src/main/oozie/full/workflow/lib/*.jar

hadoop fs -rmr ${OUTPUT_PATH}
pig -p commonJarsPath=${JARS} -p parallel=1 -p inputPath=full/hbase-dump/mproto-m* -p outputPath=${OUTPUT_PATH} -p mapredChildJavaOpts=-Xmx2000m document-similarity.pig
