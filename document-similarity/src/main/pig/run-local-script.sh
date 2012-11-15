#!/bin/bash

SCRIPT=$1
OUTPUT_PATH=$2
JARS=../../../../integration/src/main/oozie/full/workflow/lib/*.jar

hadoop fs -rm -r ${OUTPUT_PATH}

pig -p sample=0.5 -p similarityTopnDocumentPerDocument=2 -p commonJarsPath=${JARS} -p parallel=2 -p inputPath=full/hbase-dump/mproto-m* -p outputPath=${OUTPUT_PATH} -p mapredChildJavaOpts=-Xmx2000m -f ${SCRIPT}
