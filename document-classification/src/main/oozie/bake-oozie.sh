#!/bin/bash

function change() { #1: string to change #old string # new string
	echo `eval "echo ${1} | sed \"s/${2}/${3}/\""`
}

TASK=$1
PROPERTIES_INGRIDIENT=$2

TIME_INFIX=`echo -e "import time\nprint time.time()" | python`

INSCRIPT_PATH=`echo -e "x=\"$0\"\nxl = x.rfind(\"/\")\ny=x[:xl]\nprint y" | python`
cd $INSCRIPT_PATH
cd ../

cp -r -f oozie oozie_${TIME_INFIX}

TMP="oozie oozie_${TIME_INFIX}"

CTO="oozie_${TIME_INFIX}/copy-to-oozie.sh.part1"
CP="oozie_${TIME_INFIX}/${PROPERTIES_INGRIDIENT}"
###################### copy-to-oozie.ingridient.sh # WRITE # 1->2
CTO_TMP=`./python/string_replacer.py $CTO @TIME_INFIX@ ${TIME_INFIX} part1 part2`

#echo "./python/string_replacer.py $CTO @TIME_INFIX@ ${TIME_INFIX} part1 part2"
###################### cluster.properties # WRITE # 1->2
CP_TMP=`./python/string_replacer.py $CP @TIME_INFIX@ ${TIME_INFIX} part1 part2`
######################
CP=$CP_TMP
CTO=$CTO_TMP

mkdir ./TMP${TIME_INFIX}/
###################### cluster.properties # READ # 1->2
OPTS_CROSS=`python ./python/opts_checker.py ${CP} ./TMP${TIME_INFIX}/`
######################


for i in ${OPTS_CROSS};
do
	mkdir oozie_${TIME_INFIX}_OPTS_${i}
	cp -r -f oozie_${TIME_INFIX}/* oozie_${TIME_INFIX}_OPTS_${i}
	###################### copy-to-oozie.ingridient.sh # WRITE # 2->3
	CTO="oozie_${TIME_INFIX}_OPTS_${i}/copy-to-oozie.sh.part2"
	CTO=`./python/string_replacer.py $CTO @OPTS_INFIX@ ${i} part2 part3`
	###################### 
	###################### cluster.properties # WRITE # 2->3
	CP=`change "./oozie_${TIME_INFIX}_OPTS_${i}/${PROPERTIES_INGRIDIENT}" part1 part2`
	CP=`./python/opts_chooser.py  ./TMP${TIME_INFIX}/tmp_key.txt ${i} $CP part2 part3`
	###################### 
done
rm -r -f ${TMP}
rm -r -f oozie_${TIME_INFIX}
rm -r -f ./TMP${TIME_INFIX}/

if [ ! -f ../../target/document-classification-1.0-SNAPSHOT.jar ] ; then
	cd ../../
	mvn install
	cd ./src/main
fi

for i in ${OPTS_CROSS};
do
	java -cp ../../target/document-classification-1.0-SNAPSHOT.jar pl.edu.icm.coansys.classification.documents.auxil.oozieworkflowbaker.xmlworkflows.OozieWorkflowBaker  oozie_${TIME_INFIX}_OPTS_${i}/${TASK}/workflow/workflow.xml.part1 .xml.part1 .xml
done
ASD
