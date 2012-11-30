#!/bin/bash
CLASS_JAR=$1
COLLECTION_ZIPS_DIR=$2
COLLECTION_NAME=$3
COLLECTION_SEQUENCE_FILE=$4

# create sequence file and move it to HDFS (single java application)

rm -rf ${COLLECTION_SEQUENCE_FILE}
hadoop jar ${CLASS_JAR} pl.edu.icm.coansys.importers.io.writers.file.BwmetaToDocumentWraperSequenceFileWriter ${COLLECTION_ZIPS_DIR} ${COLLECTION_NAME} ${COLLECTION_SEQUENCE_FILE}

# exemplary run: ./generate-sequence-file.sh workflow/lib/importers*.jar /mnt/tmp/bwndata/bazekon-20120228/ bazekon bwndata/sequence-file/bazekon-20120228.sf
