#!/bin/bash

TASK=$1
PROPERTIES_INGRIDIENT=$2

TIME_INFIX=`echo -e "import time\nprint time.time()" | python`

INSCRIPT_PATH=`echo -e "x=\"$0\"\nxl = x.rfind(\"/\")\ny=x[:xl]\nprint y" | python`
cd $INSCRIPT_PATH
cd ../

cp -r -f oozie oozie_${TIME_INFIX}
 
./python/time_infix.py oozie_${TIME_INFIX}/copy-to-oozie.ingridient.sh @TIME_INFIX@ ${TIME_INFIX}
./python/time_infix.py oozie_${TIME_INFIX}/${PROPERTIES_INGRIDIENT} @TIME_INFIX@ ${TIME_INFIX}

mkdir ./TMP${TIME_INFIX}/
OPTS_CROSS=`python ./python/opts_checker.py oozie/${PROPERTIES_INGRIDIENT} ./TMP${TIME_INFIX}/`

for i in ${OPTS_CROSS};
do
	mkdir oozie_${TIME_INFIX}_OPTS_${i}
	cp -r -f oozie_${TIME_INFIX}/* oozie_${TIME_INFIX}_OPTS_${i}
	./python/time_infix.py oozie_${TIME_INFIX}_OPTS_${i}/copy-to-oozie.ingridient.sh @OPTS_INFIX@ ${i}
	./python/opts_chooser.py ./TMP${TIME_INFIX}/tmp_key.txt ${i} ./oozie_${TIME_INFIX}_OPTS_${i}/${PROPERTIES_INGRIDIENT}
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
	java -cp ../../target/document-classification-1.0-SNAPSHOT.jar pl.edu.icm.coansys.classification.documents.auxil.oozieworkflowbaker.xmlworkflows.OozieWorkflowBaker  oozie_${TIME_INFIX}_OPTS_${i}/${TASK}/workflow/workflow.ingridient.xml oozie_${TIME_INFIX}_OPTS_${i}/${TASK}/workflow/
done





