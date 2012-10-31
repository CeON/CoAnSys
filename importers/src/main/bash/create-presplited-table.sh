#!/bin/bash

# command-line paramters
LOCAL_INPUT_PATH=$1
HDFS_INPUT_PATH=$2
OUTPUT_PATH=$3
TABLE_NAME=$4
TABLE_COLFAMS=$5
TABLE_REGION_CNT=$6
LOAD_INPUT_TO_HDFS=$7

# global parameters
COMMONS_JAR=../../../../commons/target/commons-1.0-SNAPSHOT.jar
HDFS_KEYS_FILE=keys

export HBASE_CLASSPATH=${COMMONS_JAR}

if [ "${LOAD_INPUT_TO_HDFS}" = "true" ]; then
	hadoop fs -rm -r ${HDFS_INPUT_PATH}
	hadoop fs -mkdir ${HDFS_INPUT_PATH}
	hadoop fs -put ${LOCAL_INPUT_PATH} ${HDFS_INPUT_PATH}
fi

hadoop fs -rm -r ${OUTPUT_PATH}

time hadoop jar ${COMMONS_JAR} pl.edu.icm.coansys.commons.hbase.SequenceFileKeysSamplerMR -libjars ${COMMONS_JAR} -D sampler.samples.region.count=${TABLE_REGION_CNT} ${HDFS_INPUT_PATH} ${OUTPUT_PATH}

hadoop fs -rm ${HDFS_KEYS_FILE}
hadoop fs -mv ${OUTPUT_PATH}/part-r-00000 ${HDFS_KEYS_FILE}

time hbase pl.edu.icm.coansys.commons.hbase.HBaseTableUtils false DROP ${TABLE_NAME}
time hbase org.apache.hadoop.hbase.util.RegionSplitter -D split.algorithm=pl.edu.icm.coansys.commons.hbase.SequenceFileSplitAlgorithm -f ${TABLE_COLFAMS} -c ${TABLE_REGION_CNT} ${TABLE_NAME}
