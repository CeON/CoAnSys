#!/bin/bash

OUTPUT_PATH=$1
JARS=../../../../integration/src/main/oozie/full/workflow/lib/*.jar

hadoop fs -rmr ${OUTPUT_PATH}
pig -p commonJarsPath=${JARS} -p parallel=2 -p inputPath=grotoap10_dump/mproto-m* -p outputPath=${OUTPUT_PATH} -p mapredChildJavaOpts=-Xmx1000m stemmer.pig
