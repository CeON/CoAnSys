#!/bin/bash
MODE=$1
IMPORTERS_JAR=$2
COLLECTION_ZIPS_DIR=$3
COLLECTION_NAME=$4
COLLECTION_SEQUENCE_FILE=$5
COLLECTION_SEQUENCE_FILE_HDFS_DIR=$6
BULK_HFILE=$7
HBASE_TABLENAME=$8

echo "truncate '${HBASE_TABLENAME}'" | hbase shell

# create sequence file and move it to HDFS (single java application)
if [ "${MODE}" = "sf" ] || [ "${MODE}" = "all" ]; then
	rm -rf ${COLLECTION_SEQUENCE_FILE}
	java -cp ${IMPORTERS_JAR} pl.edu.icm.coansys.importers.io.writers.tsv.WrapperSequenceFileWriter_Bwmeta ${COLLECTION_ZIPS_DIR} ${COLLECTION_NAME} ${COLLECTION_SEQUENCE_FILE}
	hadoop fs -rm -r ${COLLECTION_SEQUENCE_FILE_HDFS_DIR}
	hadoop fs -moveFromLocal ${COLLECTION_SEQUENCE_FILE} ${COLLECTION_SEQUENCE_FILE_HDFS_DIR}
fi

# create hfile (mapreduce job)
if [ "${MODE}" = "hfile" ] || [ "${MODE}" = "all" ]; then
	hadoop fs -rm -r ${BULK_HFILE}
	hadoop jar ${IMPORTERS_JAR} pl.edu.icm.coansys.importers.io.writers.hbase.DocumentWrapperSequenceFileToHBase -Dbulk.output=${BULK_HFILE} ${COLLECTION_SEQUENCE_FILE_HDFS_DIR} ${HBASE_TABLENAME}
	hadoop fs -chmod -R 777 ${BULK_HFILE}
fi

# complete bulk loading
if [ "${MODE}" = "bulk" ] || [ "${MODE}" = "all" ]; then
	echo "count '${HBASE_TABLENAME}'" | hbase shell
	hadoop jar /usr/lib/hbase/hbase-0.92.1-cdh4.0.1-security.jar completebulkload ${BULK_HFILE} ${HBASE_TABLENAME}
	echo "count '${HBASE_TABLENAME}'" | hbase shell
	
	# clean temporary directories
	hadoop fs -rm -r ${COLLECTION_SEQUENCE_FILE_HDFS_DIR}
	hadoop fs -rm -r ${BULK_HFILE}
fi
