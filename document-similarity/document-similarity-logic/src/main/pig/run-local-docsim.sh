#!/bin/bash

SCRIPT_TFIDF=document-tfidf-p1.pig
SCRIPT_DOCSIM=document-similarity-p2.pig
OUTPUT_PATH=document-similarity
JARS=../../../../integration/src/main/oozie/full/workflow/lib/*.jar

TFIDF_INPUT_PATH="${OUTPUT_PATH}/tfidf/all-topn"
PARALLEL=2

hadoop fs -rm -r ${OUTPUT_PATH}
time pig -p tfidfMinValue=0.0 -p sample=1.0 -p commonJarsPath=${JARS} -p parallel=${PARALLEL} -p inputPath=full/hbase-dump/mproto-m* -p outputPath=${OUTPUT_PATH} -p mapredChildJavaOpts=-Xmx2000m -f ${SCRIPT_TFIDF}
time pig -p commonJarsPath=${JARS} -p similarityTopnDocumentPerDocument=3 -p parallel=${PARALLEL} -p inputPath=${TFIDF_INPUT_PATH} -p outputPath=${OUTPUT_PATH} -p mapredChildJavaOpts=-Xmx2000m -f ${SCRIPT_DOCSIM}

