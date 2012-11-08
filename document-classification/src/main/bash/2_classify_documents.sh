#!/bin/bash

#
# (C) 2010-2012 ICM UW. All rights reserved.
#

INSCRIPT_PATH=`echo -e "x=\"$0\"\nxl = x.rfind(\"/\")\ny=x[:xl]\nprint y" | python`
cd $INSCRIPT_PATH
eval "cd ../pig"

TABLE_TO_CLASSIFY=${1}
MODEL_HDFS="${2}"
RESULT_HDFS="${3}"
FEATURE_VECTOR=${4}
SIMILARITY_METRIC=${5}
NUMBER_OF_DOC_NEIGHBOURS=${6}

echo "~~~~~~~~~~~~~~~~~~~~~~~!!!!!!!!!!!!!!!!!!!!!!~~~~~~~~~~~~~~~~~~~~~~~~~"

echo "hadoop dfs -rm -r -f ${RESULT_HDFS}"
eval "hadoop dfs -rm -r -f ${RESULT_HDFS}"

echo "pig -x mapred -p DEF_SRC=${TABLE_TO_CLASSIFY} -p inMo=${MODEL_HDFS} -p DEF_DST=${RESULT_HDFS} -p featurevector=${FEATURE_VECTOR} -p simmeth=${SIMILARITY_METRIC} -p neigh=${NUMBER_OF_DOC_NEIGHBOURS} 2_MODEL_USE_classify.pig"
eval "time pig -x mapred -p DEF_SRC=${TABLE_TO_CLASSIFY} -p inMo=${MODEL_HDFS} -p DEF_DST=${RESULT_HDFS} -p featurevector=${FEATURE_VECTOR} -p simmeth=${SIMILARITY_METRIC} -p neigh=${NUMBER_OF_DOC_NEIGHBOURS} 2_MODEL_USE_classify.pig"

