#!/bin/bash

# ------------------------------------------------------------------
# Generate one sequence file from the content of directory containing bwmeta zip archives (works as a single threaded application)
# ------------------------------------------------------------------

# ------------------------------------------------------------------
# Example:
# ./import-via-sequence-files.sh ../../../target/importers-1.0-SNAPSHOT-jar-with-dependencies.jar /mnt/tmp/bwndata/bazekon-20120228 bazekon /mnt/tmp/bazekon-20120228.sf.snappy true
# ------------------------------------------------------------------

# ------------------------------------------------------------------
# command-line parameters			# possible value
# ------------------------------------------------------------------
IMPORTERS_JAR=$1				# ../../../target/importers-1.0-SNAPSHOT-jar-with-dependencies.jar
BWNDATA_ZIPS_INPUT_DIR=$2			# /mnt/tmp/bwndata/bazekon-20120228
BWNDATA_COLLECTION_NAME=$3			# bazekon
BWNDATA_OUTPUT_SEQUENCE_FILE=$4			# /mnt/tmp/bazekon-20120228.sf.snappy
IS_SNAPPY_COMPRESSED=$5				# true

# ------------------------------------------------------------------
# global variable
# ------------------------------------------------------------------
LOG_FILE=report.log

# may be needed for native compression
export LD_LIBRARY_PATH=${LD_LIBRARY_PATH}:/usr/lib/hadoop/lib/native/

rm -rf ${BWNDATA_OUTPUT_SEQUENCE_FILE}

java -cp ${IMPORTERS_JAR} pl.edu.icm.coansys.importers.io.writers.file.BwmetaToDocumentWraperSequenceFileWriter ${BWNDATA_ZIPS_INPUT_DIR} ${BWNDATA_COLLECTION_NAME} ${BWNDATA_OUTPUT_SEQUENCE_FILE} ${IS_SNAPPY_COMPRESSED}
