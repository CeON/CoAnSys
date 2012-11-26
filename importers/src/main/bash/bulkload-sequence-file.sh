#!/bin/bash

# command-line parameters
MODE=$1
IMPORTERS_JAR=$2
HDFS_SEQUENCE_FILE_INPUT_DIR=$3
HDFS_BULK_HFILE_OUTPUT_DIR=$4
HBASE_TABLENAME=$5

# create hfile (mapreduce job)
if [ "${MODE}" = "hfile" ] || [ "${MODE}" = "all" ]; then
        echo "truncate '${HBASE_TABLENAME}'" | hbase shell
        hadoop fs -rm -r ${HDFS_BULK_HFILE_OUTPUT_DIR}
        hadoop jar ${IMPORTERS_JAR} pl.edu.icm.coansys.importers.io.writers.hbase.DocumentWrapperSequenceFileToHBase -Dbulk.output=${HDFS_BULK_HFILE_OUTPUT_DIR} ${HDFS_SEQUENCE_FILE_INPUT_DIR} ${HBASE_TABLENAME}
        hadoop fs -chmod -R 777 ${HDFS_BULK_HFILE_OUTPUT_DIR}
fi

# complete bulk loading
if [ "${MODE}" = "bulk" ] || [ "${MODE}" = "all" ]; then
        hadoop jar /usr/lib/hbase/hbase-0.92.1-cdh4.0.1-security.jar completebulkload ${HDFS_BULK_HFILE_OUTPUT_DIR} ${HBASE_TABLENAME}
fi

