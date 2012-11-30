#!/bin/bash

# ------------------------------------------------------------------
# Create HBase table presplited by region boundaries based on samples from a dataset that is to be imported
# ------------------------------------------------------------------

# ------------------------------------------------------------------
# Example: 
# ./create-presplited-custom-table.sh ../../../target/importers-1.0-SNAPSHOT-jar-with-dependencies.jar /bwndata/seqfile/medline-20100716.sf.* medline_bwndata 10
# ------------------------------------------------------------------

# ------------------------------------------------------------------
# command-line parameters                       # possible valus
# ------------------------------------------------------------------
IMPORTERS_JAR=$1				# ../../../target/importers-1.0-SNAPSHOT-jar-with-dependencies.jar
HDFS_INPUT_PATH=$2				# /bwndata/seqfile/medline-20100716.sf.*	
TABLE_NAME=$3					# medline_bwndata
TABLE_REGION_CNT=$4				# 10

# ------------------------------------------------------------------
# global parameters
# ------------------------------------------------------------------
SAMPLES_OUTPUT_PATH=${HDFS_INPUT_PATH}_samples_${TABLE_NAME}
LOCAL_KEYS_FILE=keys

export HBASE_CLASSPATH=${IMPORTERS_JAR}

# (potentially) remove output directory
hadoop fs -rm -r ${SAMPLES_OUTPUT_PATH}
# run MapReduce sampler
hadoop jar ${IMPORTERS_JAR} pl.edu.icm.coansys.commons.hbase.SequenceFileKeysSamplerMR -libjars ${IMPORTERS_JAR} -D sampler.samples.region.count=${TABLE_REGION_CNT} ${HDFS_INPUT_PATH} ${SAMPLES_OUTPUT_PATH}

# download the file containing region boundaries
hadoop fs -get ${SAMPLES_OUTPUT_PATH}/part-r-00000
mv part-r-00000 ${LOCAL_KEYS_FILE}

# ------------------------------------------------------------------
# specify HERE what table you want to create here
# ------------------------------------------------------------------
hbase pl.edu.icm.coansys.commons.hbase.HBaseTableUtils false DROP ${TABLE_NAME}
exec hbase shell <<EOF
	create "${TABLE_NAME}", {NAME => 'f1', VERSIONS => 1, TTL => 2592000, BLOCKCACHE => true}, {SPLITS_FILE => "${LOCAL_KEYS_FILE}"}
EOF

# cleanup
hadoop fs -rm -r ${SAMPLES_OUTPUT_PATH}
rm ${LOCAL_KEYS_FILE}
