#!/bin/bash

SCRIPT_TFIDF=document-tfidf-p1.pig
SCRIPT_DOCSIM=document-similarity-p2.pig
OUTPUT_PATH=document-similarity
JARS=../../../../integration/src/main/oozie/full/workflow/lib/*.jar

TFIDF_INPUT_PATH="${OUTPUT_PATH}/tfidf/all-topn"
PARALLEL=40

hadoop fs -rm -r ${OUTPUT_PATH}
time pig -p tmpCompressionCodec=lzo -p tfidfMinValue=0.25 -p sample=1.0 -p commonJarsPath=${JARS} -p parallel=${PARALLEL} -p inputPath=full/hbase-dump/mproto-m* -p outputPath=${OUTPUT_PATH} -p mapredChildJavaOpts=-Xmx10000m -f ${SCRIPT_TFIDF}
time pig -p tmpCompressionCodec=lzo -p commonJarsPath=${JARS} -p similarityTopnDocumentPerDocument=20 -p parallel=${PARALLEL} -p inputPath=${TFIDF_INPUT_PATH} -p outputPath=${OUTPUT_PATH} -p mapredChildJavaOpts=-Xmx10000m -f ${SCRIPT_DOCSIM}

