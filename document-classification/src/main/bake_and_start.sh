#!/bin/bash

HOW="$1"
NUM="$2"
RM="$3"

if [ "${NUM}" == "" ]; then
	NUM=1
fi



function change() { #1: string to change #old string # new string
	echo `eval "echo ${1} | sed \"s/${2}/${3}/\""`
}




cd oozie
#echo "#################"
#echo `pwd`
#echo "#################"
./bake-oozie.sh dc-train-model/ dc-train-model/cluster.properties.part1 $RM
cd ../

if [ "$HOW" == "NUM" ]; then
	LATTEST_OOZIE=`ls -t1 | head -n${NUM}`
	for i in $LATTEST_OOZIE; do
		cd $i
		./submit-to-oozie.sh dc-train-model/ pdendek hadoop-master dc-train-model/cluster.properties 	
		cd ..
	done
elif [ "$HOW" == "ALL" ]; then
	LATTEST_OOZIE=`ls | grep oozie_`
	for i in $LATTEST_OOZIE; do
		cd $i
		./submit-to-oozie.sh dc-train-model/ pdendek hadoop-master dc-train-model/cluster.properties 	
		cd ..
	done
else
	LATTEST_OOZIE=`ls | grep oozie_`
	for i in $LATTEST_OOZIE; do
		cd $i
		./submit-to-oozie.sh dc-train-model/ pdendek hadoop-master dc-train-model/cluster.properties 	
		cd ..
	done	
fi



#rm -r -f oozie_*

