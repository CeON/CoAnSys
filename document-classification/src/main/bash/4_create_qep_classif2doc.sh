#!/bin/bash

#
# (C) 2010-2012 ICM UW. All rights reserved.
#

eval "cd ../pig/4_QEP"

SRC=${1} #e.g.=/user/pdendek/parts/alg_doc_classif
DST=${2} #e.g.=_result_docclassif_CodeByDoc

echo "~~~~~~~~~~~~~~~~~~~~~~~!!!!!!!!!!!!!!!!!!!!!!~~~~~~~~~~~~~~~~~~~~~~~~~"

echo "hadoop dfs -rm -r -f ${DST}"
eval "hadoop dfs -rm -r -f ${DST}"

echo "pig -x mapred -p DEF_SRC=${SRC} -p DEF_DST=${DST} qep_classif_doc.pig"
eval "pig -x mapred -p DEF_SRC=${SRC} -p DEF_DST=${DST} qep_classif_doc.pig"
