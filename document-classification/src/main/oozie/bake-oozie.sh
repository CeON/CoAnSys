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

CTO_T=`./python/string_replacer.py $CTO @TIME_INFIX@ ${TIME_INFIX} part1 part2`
rm $CTO; 
CTO=$CTO_T

CP=`./python/string_replacer.py $CP @TIME_INFIX@ ${TIME_INFIX} part1 part2`

mkdir ./TMP${TIME_INFIX}/
OPTS_CROSS=`python ./python/opts_checker.py ${CP} ./TMP${TIME_INFIX}/`

for i in ${OPTS_CROSS};
do
	mkdir oozie_${TIME_INFIX}_OPTS_${i}
	cp -r -f oozie_${TIME_INFIX}/* oozie_${TIME_INFIX}_OPTS_${i}
	###################### copy-to-oozie.ingridient.sh # WRITE # 2->3
	CTO="oozie_${TIME_INFIX}_OPTS_${i}/copy-to-oozie.sh.part2"
	CTO_T=`./python/string_replacer.py $CTO @OPTS_INFIX@ ${i} part2 part3`
	rm $CTO
	mv $CTO_T `change "${CTO_T}" ".sh.part3" ".sh"`
	###################### cluster.properties # WRITE # 2->3
	CP1="./oozie_${TIME_INFIX}_OPTS_${i}/${PROPERTIES_INGRIDIENT}"
	CP2=`change $CP1 part1 part2`
	CP3=`./python/string_replacer.py $CP2 @OPTS_INFIX@ ${i} part2 part3`
	CP4=`./python/opts_chooser.py ./TMP${TIME_INFIX}/tmp_key.txt ${i} $CP3 part3 part4`
	rm ${CP1}
	rm ${CP2}
	rm ${CP3}
	mv ${CP4} `change "${CP4}" "ties.part4" "ties"`

	chmod +x ./oozie_${TIME_INFIX}_OPTS_${i}/submit-to-oozie.sh
	chmod +x ./oozie_${TIME_INFIX}_OPTS_${i}/copy-to-oozie.sh
	rm ./oozie_${TIME_INFIX}_OPTS_${i}/bake-oozie.sh
done

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
	rm oozie_${TIME_INFIX}_OPTS_${i}/${TASK}/workflow/workflow.xml.part1
done

