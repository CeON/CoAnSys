#!/bin/bash

# command-line parameters
MODE=$1
IMPORTERS_JAR=$2
COLLECTION_SEQUENCE_FILE_HDFS_DIR=$3
BULK_HFILE=$4
HBASE_TABLENAME=$5

# create hfile (mapreduce job)
if [ "${MODE}" = "hfile" ] || [ "${MODE}" = "all" ]; then
        echo "truncate '${HBASE_TABLENAME}'" | hbase shell
        hadoop fs -rm -r ${COLLECTION_SEQUENCE_FILE_HDFS_DIR}
        hadoop fs -rm -r ${BULK_HFILE}
        hadoop jar ${IMPORTERS_JAR} pl.edu.icm.coansys.importers.io.writers.hbase.DocumentWrapperSequenceFileToHBase -Dbulk.output=${BULK_HFILE} ${COLLECTION_SEQUENCE_FILE_HDFS_DIR} ${HBASE_TABLENAME}
        hadoop fs -chmod -R 777 ${BULK_HFILE}
fi

# complete bulk loading
if [ "${MODE}" = "bulk" ] || [ "${MODE}" = "all" ]; then
        hadoop jar /usr/lib/hbase/hbase-0.92.1-cdh4.0.1-security.jar completebulkload ${BULK_HFILE} ${HBASE_TABLENAME}
        # clean temporary directories
        hadoop fs -rm -r ${COLLECTION_SEQUENCE_FILE_HDFS_DIR}
        hadoop fs -rm -r ${BULK_HFILE}
fi

