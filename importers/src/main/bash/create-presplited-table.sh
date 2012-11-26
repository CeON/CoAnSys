#!/bin/bash

# Create HBase table presplited by regions based on sampling dataset to be imported

# required command-line paramters
IMPORTERS_JAR=$1
HDFS_INPUT_PATH=$2
SAMPLES_OUTPUT_PATH=$3
TABLE_NAME=$4
TABLE_COLFAMS=$5
TABLE_REGION_CNT=$6

# optional parameters
LOAD_INPUT_TO_HDFS=$7
LOCAL_INPUT_PATH=$8

# global parameters
HDFS_KEYS_FILE=keys

export HBASE_CLASSPATH=${IMPORTERS_JAR}

if [ "${LOAD_INPUT_TO_HDFS}" = "true" ]; then
	hadoop fs -rm -r ${HDFS_INPUT_PATH}
	hadoop fs -mkdir ${HDFS_INPUT_PATH}
	hadoop fs -put ${LOCAL_INPUT_PATH} ${HDFS_INPUT_PATH}
fi

hadoop fs -rm -r ${SAMPLES_OUTPUT_PATH}

hadoop jar ${IMPORTERS_JAR} pl.edu.icm.coansys.commons.hbase.SequenceFileKeysSamplerMR -libjars ${IMPORTERS_JAR} -D sampler.samples.region.count=${TABLE_REGION_CNT} ${HDFS_INPUT_PATH} ${SAMPLES_OUTPUT_PATH}

hadoop fs -rm ${HDFS_KEYS_FILE}
hadoop fs -mv ${SAMPLES_OUTPUT_PATH}/part-r-00000 ${HDFS_KEYS_FILE}

# potentially drop and re-create table
hbase pl.edu.icm.coansys.commons.hbase.HBaseTableUtils false DROP ${TABLE_NAME}
hbase org.apache.hadoop.hbase.util.RegionSplitter -D split.algorithm=pl.edu.icm.coansys.commons.hbase.SequenceFileSplitAlgorithm -f ${TABLE_COLFAMS} -c ${TABLE_REGION_CNT} ${TABLE_NAME}
