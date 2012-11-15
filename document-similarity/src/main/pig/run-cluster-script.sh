#!/bin/bash

SCRIPT=$1
OUTPUT_PATH=$2
JARS=../../../../integration/src/main/oozie/full/workflow/lib/*.jar

hadoop fs -rm -r ${OUTPUT_PATH}

pig -p sample=1.0 -p similarityTopnDocumentPerDocument=20 -p commonJarsPath=${JARS} -p parallel=30 -p inputPath=full/hbase-dump/mproto-m* -p outputPath=${OUTPUT_PATH} -p mapredChildJavaOpts=-Xmx8000m -f ${SCRIPT}
