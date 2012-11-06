#!/bin/bash

#
# (C) 2010-2012 ICM UW. All rights reserved.
#

eval "cd ../pig"

DICTIONARY_HBASE=${1}
DICTIONARY_HDFS=${2}

echo "~~~~~~~~~~~~~~~~~~~~~~~!!!!!!!!!!!!!!!!!!!!!!~~~~~~~~~~~~~~~~~~~~~~~~~"

echo "hadoop dfs -rm -r -f ${DICTIONARY_HDFS}"
eval "hadoop dfs -rm -r -f ${DICTIONARY_HDFS}"

echo "pig -x mapred -p DEF_SRC=${DICTIONARY_HBASE} -p DEF_DST=${DICTIONARY_HDFS} 3_MAP_ROWID_BWID_create_mapping.pig "
eval "pig -x mapred -p DEF_SRC=${DICTIONARY_HBASE} -p DEF_DST=${DICTIONARY_HDFS} 3_MAP_ROWID_BWID_create_mapping.pig "
