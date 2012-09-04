#!/bin/bash
SCRIPT_CLASSPATH=$1
COLLECTION_ZIPS_DIR=$2
COLLECTION_NAME=$3
COLLECTION_SEQUENCE_FILE=$4
COLLECTION_SEQUENCE_FILE_HDFS_DIR=$5

# create sequence file and move it to HDFS (single java application)

rm -rf ${COLLECTION_SEQUENCE_FILE}
java -cp ${SCRIPT_CLASSPATH} pl.edu.icm.coansys.importers.io.writers.tsv.WrapperSequenceFileWriter_Bwmeta ${COLLECTION_ZIPS_DIR} ${COLLECTION_NAME} ${COLLECTION_SEQUENCE_FILE}
hadoop fs -moveFromLocal ${COLLECTION_SEQUENCE_FILE} ${COLLECTION_SEQUENCE_FILE_HDFS_DIR}

# exemplary run: ./generate-sequence-file.sh workflow/lib/importers*.jar /mnt/tmp/bwndata/bazekon-20120228/ bazekon ./bazekon-20120228.sf bwndata/sequence-file/
