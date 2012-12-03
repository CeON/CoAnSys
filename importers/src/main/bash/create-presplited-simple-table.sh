#!/bin/bash

# ------------------------------------------------------------------
# Create HBase table presplited by region boundaries based on samples from a dataset that is to be imported
# ------------------------------------------------------------------

# ------------------------------------------------------------------
# Example: 
# ./create-presplited-simple-table.sh ../../../target/importers-1.0-SNAPSHOT-jar-with-dependencies.jar /bwndata/seqfile/medline-20100716.sf.* medline_bwndata m:c 10
# ------------------------------------------------------------------

# ------------------------------------------------------------------
# command-line parameters                       # possible valus
# ------------------------------------------------------------------
IMPORTERS_JAR=$1				../../../target/importers-1.0-SNAPSHOT-jar-with-dependencies.jar
HDFS_INPUT_PATH=$2				/bwndata/seqfile/medline-20100716.sf.*	
TABLE_NAME=$3					medline_bwndata
TABLE_COLFAMS=$4				m:c
TABLE_REGION_CNT=$5				10

# ------------------------------------------------------------------
# global parameters
# ------------------------------------------------------------------
SAMPLES_OUTPUT_PATH=${HDFS_INPUT_PATH}_samples_${TABLE_NAME}
HDFS_KEYS_FILE=keys

export HBASE_CLASSPATH=${IMPORTERS_JAR}

# (potentially) remove some output directories
hadoop fs -rm -r ${SAMPLES_OUTPUT_PATH}
hadoop fs -rm ${HDFS_KEYS_FILE}

# run MapReduce sampler (and rename the results to HDFS_KEYS_FILE
hadoop jar ${IMPORTERS_JAR} pl.edu.icm.coansys.commons.hbase.SequenceFileKeysSamplerMR -libjars ${IMPORTERS_JAR} -D sampler.samples.region.count=${TABLE_REGION_CNT} ${HDFS_INPUT_PATH} ${SAMPLES_OUTPUT_PATH}
hadoop fs -mv ${SAMPLES_OUTPUT_PATH}/part-r-00000 ${HDFS_KEYS_FILE}

# potentially drop and re-create table
hbase pl.edu.icm.coansys.commons.hbase.HBaseTableUtils false DROP ${TABLE_NAME}
hbase org.apache.hadoop.hbase.util.RegionSplitter -D split.algorithm=pl.edu.icm.coansys.commons.hbase.SequenceFileSplitAlgorithm -f ${TABLE_COLFAMS} -c ${TABLE_REGION_CNT} ${TABLE_NAME}

# cleanup
hadoop fs -rm -r ${SAMPLES_OUTPUT_PATH}
hadoop fs -rm ${HDFS_KEYS_FILE}
