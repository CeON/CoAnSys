#!/bin/bash

# ------------------------------------------------------------------
Loads the data from Sequence Files into HBase table as MR map-only job.
The mapper class can be passed as parameter. 
Load can be done in two ways:
1) using PUT API

To use PUT API,
MODE should be "hfileput"
HDFS_BULK_HFILE_OUTPUT_DIR should be ommited
Example:


2) using bulkloading

To use bulkload,
HDFS_BULK_HFILE_OUTPUT_DIR should be specified
Example:


# ------------------------------------------------------------------
# command-line parameters                       # possible values
# ------------------------------------------------------------------
IMPORTERS_JAR=$1
MODE=$2
HDFS_SEQUENCE_FILE_INPUT_DIR=$3
HBASE_TABLENAME=$4
BULKLOAD_MAPPER=$5

# ------------------------------------------------------------------
# optional command-line parameters		# possible values
# ------------------------------------------------------------------
HBASE_FULL_COLUMN_NAME=$6
HDFS_BULK_HFILE_OUTPUT_DIR=$7

# create hfile (mapreduce job)
if [ "${MODE}" = "hfileput" ] || [ "${MODE}" = "all" ]; then
        hadoop fs -rm -r ${HDFS_BULK_HFILE_OUTPUT_DIR}
        hadoop jar ${IMPORTERS_JAR} pl.edu.icm.coansys.importers.io.writers.hbase.DocumentWrapperSequenceFileToHBase -D hbase.table.full.column.name=${HBASE_FULL_COLUMN_NAME} -D mapreduce.map.class=${BULKLOAD_MAPPER} -D bulk.output=${HDFS_BULK_HFILE_OUTPUT_DIR} ${HDFS_SEQUENCE_FILE_INPUT_DIR} ${HBASE_TABLENAME}
        hadoop fs -chmod -R 777 ${HDFS_BULK_HFILE_OUTPUT_DIR}
fi

# complete bulk loading
if [ "${MODE}" = "bulk" ] || [ "${MODE}" = "all" ]; then
        hadoop jar /usr/lib/hbase/hbase-0.92.1-cdh4.0.1-security.jar completebulkload ${HDFS_BULK_HFILE_OUTPUT_DIR} ${HBASE_TABLENAME}
fi
