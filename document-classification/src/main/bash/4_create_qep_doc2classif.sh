#!/bin/bash

#
# (C) 2010-2012 ICM UW. All rights reserved.
#

INSCRIPT_PATH=`echo -e "x=\"$0\"\nxl = x.rfind(\"/\")\ny=x[:xl]\nprint y" | python`
cd $INSCRIPT_PATH
eval "cd ../pig"

SRC=${1} #e.g.=/user/pdendek/parts/alg_doc_classif
DST=${2} #e.g.=_result_docclassif_CodeByDoc

echo "~~~~~~~~~~~~~~~~~~~~~~~!!!!!!!!!!!!!!!!!!!!!!~~~~~~~~~~~~~~~~~~~~~~~~~"

echo "hadoop dfs -rm -r -f ${DST}"
eval "hadoop dfs -rm -r -f ${DST}"

echo "pig -x mapred -p DEF_SRC=${SRC} -p DEF_DST=${DST} 4_QEP_qep_doc_classif.pig"
eval "time pig -x mapred -p DEF_SRC=${SRC} -p DEF_DST=${DST} 4_QEP_qep_doc_classif.pig"
