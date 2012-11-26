#!/bin/bash

# Convert Bwndata from zips into Hadoop Sequence File (works as a single threaded application)
# Example:
# ./import-via-sequence-files.sh ../../../target/importers-1.0-SNAPSHOT-jar-with-dependencies.jar /mnt/tmp/bwndata/bazekon-20120228 bazekon /mnt/tmp/bazekon-20120228.sf true
# ./import-via-sequence-files.sh ../../../target/importers-1.0-SNAPSHOT-jar-with-dependencies.jar /mnt/tmp/bwndata/medline-20100716 medline /mnt/tmp/medline-20100716.sf true

# may be needed for native compression
export LD_LIBRARY_PATH=${LD_LIBRARY_PATH}:/usr/lib/hadoop/lib/native/

# command-line parameters
IMPORTERS_JAR=$1
BWNDATA_ZIPS_INPUT_DIR=$2
BWNDATA_COLLECTION_NAME=$3
BWNDATA_OUTPUT_SEQUENCE_FILE=$4
IS_SNAPPY_COMPRESSED=$5

LOG_FILE=report.log

# create sequence file and move it to HDFS (single java application)
rm -rf ${LOG_FILE}
rm -rf ${BWNDATA_OUTPUT_SEQUENCE_FILE}
java -cp ${IMPORTERS_JAR} pl.edu.icm.coansys.importers.io.writers.file.BwmetaToDocumentWraperSequenceFileWriter ${BWNDATA_ZIPS_INPUT_DIR} ${BWNDATA_COLLECTION_NAME} ${BWNDATA_OUTPUT_SEQUENCE_FILE} ${IS_SNAPPY_COMPRESSED}
