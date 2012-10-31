#!/bin/bash

# command-line parameters
LOCAL_BWNDATA_SEQUENCE_FILE=$1
TABLE_NAME=$2

# default parameters
COLLECTION_SEQUENCE_FILE_HDFS_DIR=/user/akawa/${TABLE_NAME}_sf
TABLE_HFILES_HDFS_DIR=${TABLE_NAME}_hfiles
TABLE_REGION_CNT=20

time ./create-presplited-table.sh ${LOCAL_BWNDATA_SEQUENCE_FILE} ${COLLECTION_SEQUENCE_FILE_HDFS_DIR} hbasekeys presplited m:c ${TABLE_REGION_CNT} false
time ./bulkload-sequence-file.sh all ${COLLECTION_SEQUENCE_FILE_HDFS_DIR} ${TABLE_HFILES_HDFS_DIR} ${TABLE_NAME}
