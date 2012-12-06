#!/bin/bash

# ------------------------------------------------------------------
# Loads the data from Sequence Files into HBase table as MR map-only job.
# The mapper class can be passed as parameter. 
# 
# There are three available modes:
# 1) put
#    It just uses hbase put api in mapper. Might be appropriate for small 
#    amounts of data.
# 
#    Example:
#    ./bulkload-sequence-file.sh \
#      ../../../target/importers-1.0-SNAPSHOT-jar-with-dependencies.jar \
#      put \
#      pic-out \
#      matfed_pic_out \
#      pl.edu.icm.coansys.importers.io.writers.hbase.BytesWritableSequenceFileToHBasePutMapper \
#      picres:picresproto
#
# 2) hfile
#    Creates HFile which can be later bulkloaded to HBase.
#    HDFS_BULK_HFILE_OUTPUT_DIR should be specified
# 
#    Example:
#    ./bulkload-sequence-file.sh \
#      ../../../target/importers-1.0-SNAPSHOT-jar-with-dependencies.jar \
#      hfile \
#      pic-out \
#      matfed_pic_out \
#      pl.edu.icm.coansys.importers.io.writers.hbase.BytesWritableSequenceFileToHBasePutMapper \
#      picres:picresproto \
#      hfile-bulk
#
# 3) bulk
#    Creates HFile and bulkloads it.
#    HDFS_BULK_HFILE_OUTPUT_DIR should be specified
#
#    Example:
#    ./bulkload-sequence-file.sh \
#      ../../../target/importers-1.0-SNAPSHOT-jar-with-dependencies.jar \
#      bulk \
#      pic-out \
#      matfed_pic_out \
#      pl.edu.icm.coansys.importers.io.writers.hbase.BytesWritableSequenceFileToHBasePutMapper \
#      picres:picresproto \
#      hfile-bulk
#
# ------------------------------------------------------------------
# command-line parameters                       # possible values
# ------------------------------------------------------------------
IMPORTERS_JAR=$1
MODE=$2
HDFS_SEQUENCE_FILE_INPUT_DIR=$3
HBASE_TABLENAME=$4
BULKLOAD_MAPPER=$5
HBASE_FULL_COLUMN_NAME=$6

# ------------------------------------------------------------------
# optional command-line parameters		# possible values
# ------------------------------------------------------------------
HDFS_BULK_HFILE_OUTPUT_DIR=$7

# use puts
if [ "${MODE}" = "put" ] ; then
        hadoop jar ${IMPORTERS_JAR} pl.edu.icm.coansys.importers.io.writers.hbase.DocumentWrapperSequenceFileToHBase -D hbase.table.full.column.name=${HBASE_FULL_COLUMN_NAME} -D mapreduce.map.class=${BULKLOAD_MAPPER} ${HDFS_SEQUENCE_FILE_INPUT_DIR} ${HBASE_TABLENAME}
fi

# create hfile (mapreduce job)
if [ "${MODE}" = "bulk" ] || [ "${MODE}" = "hfile" ]; then
        hadoop fs -rm -r ${HDFS_BULK_HFILE_OUTPUT_DIR}
        hadoop jar ${IMPORTERS_JAR} pl.edu.icm.coansys.importers.io.writers.hbase.DocumentWrapperSequenceFileToHBase -D hbase.table.full.column.name=${HBASE_FULL_COLUMN_NAME} -D mapreduce.map.class=${BULKLOAD_MAPPER} -D bulk.output=${HDFS_BULK_HFILE_OUTPUT_DIR} ${HDFS_SEQUENCE_FILE_INPUT_DIR} ${HBASE_TABLENAME}
        hadoop fs -chmod -R 777 ${HDFS_BULK_HFILE_OUTPUT_DIR}
fi

# complete bulk loading
if [ "${MODE}" = "bulk" ]; then
        hadoop jar /usr/lib/hbase/hbase-0.92.1-cdh4.0.1-security.jar completebulkload ${HDFS_BULK_HFILE_OUTPUT_DIR} ${HBASE_TABLENAME}
fi
