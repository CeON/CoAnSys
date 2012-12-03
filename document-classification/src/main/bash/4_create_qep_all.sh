#!/bin/bash

#
# (C) 2010-2012 ICM UW. All rights reserved.
#

INSCRIPT_PATH=`echo -e "x=\"$0\"\nxl = x.rfind(\"/\")\ny=x[:xl]\nprint y" | python`
cd $INSCRIPT_PATH
HERE=`pwd`

SRC=${1} #e.g.=/user/pdendek/parts/alg_doc_classif
DST_DOC_CLASSIF=${2} #e.g.=_result_docclassif_CodeByDoc
DST_CLASSIF_DOC=${3} #e.g.=_result_docclassif_DocByCode

eval "./4_create_qep_classif2doc.sh ${SRC} ${DST_CLASSIF_DOC}"
cd ${HERE}
eval "./4_create_qep_doc2classif.sh ${SRC} ${DST_DOC_CLASSIF}"
