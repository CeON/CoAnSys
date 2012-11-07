#!/bin/bash

#
# (C) 2010-2012 ICM UW. All rights reserved.
#

INSCRIPT_PATH=`echo -e "x=\"$0\"\nxl = x.rfind(\"/\")\ny=x[:xl]\nprint y" | python`
cd $INSCRIPT_PATH
eval "cd ../pig"

DICTIONARY_HDFS=${1}
TO_TRANSLATE=${2}
TRANSLATED=${3}

echo "~~~~~~~~~~~~~~~~~~~~~~~!!!!!!!!!!!!!!!!!!!!!!~~~~~~~~~~~~~~~~~~~~~~~~~"

echo "hadoop dfs -rm -r -f ${TRANSLATED}"
eval "hadoop dfs -rm -r -f ${TRANSLATED}"

echo "pig -x mapred -p DEF_TO_TRANSLATE=${TO_TRANSLATE} -p DEF_DICTIONARY=${DICTIONARY_HDFS} -p DEF_DST=${TRANSLATED} 3_MAP_ROWID_BWID_proceed_mapping.pig"
eval "pig -x mapred -p DEF_TO_TRANSLATE=${TO_TRANSLATE} -p DEF_DICTIONARY=${DICTIONARY_HDFS} -p DEF_DST=${TRANSLATED} 3_MAP_ROWID_BWID_proceed_mapping.pig"
