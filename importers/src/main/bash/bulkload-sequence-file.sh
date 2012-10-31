#!/bin/bash

# command-line parameters
MODE=$1
COLLECTION_SEQUENCE_FILE_HDFS_DIR=$2
TABLE_NAME_HFILE_HDFS_DIR=$3
HBASE_TABLENAME=$4

IMPORTERS_JAR=../../../target/importers-1.0-SNAPSHOT.jar

# create hfile (mapreduce job)
if [ "${MODE}" = "hfile" ] || [ "${MODE}" = "all" ]; then
	hadoop fs -rm -r ${TABLE_NAME_HFILE_HDFS_DIR}
	# skip -Dbulk.output option to use HBase Put method
	time hadoop jar ${IMPORTERS_JAR} pl.edu.icm.coansys.importers.io.writers.hbase.DocumentWrapperSequenceFileToHBase -Dbulk.output=${TABLE_NAME_HFILE_HDFS_DIR} ${COLLECTION_SEQUENCE_FILE_HDFS_DIR} ${HBASE_TABLENAME}
	hadoop fs -chmod -R 777 ${TABLE_NAME_HFILE_HDFS_DIR}
fi

# complete bulk loading
if [ "${MODE}" = "bulk" ] || [ "${MODE}" = "all" ]; then
	time hadoop jar /usr/lib/hbase/hbase-0.92.1-cdh4.0.1-security.jar completebulkload ${TABLE_NAME_HFILE_HDFS_DIR} ${HBASE_TABLENAME}
	echo "count '${HBASE_TABLENAME}'" | hbase shell
fi
