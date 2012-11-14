#!/bin/bash

RM=$1

cd oozie
#echo "#################"
#echo `pwd`
#echo "#################"
./bake-oozie.sh dc-train-model/ dc-train-model/cluster.properties.part1 $RM
cd ../
LATTEST_OOZIE=`ls -t1 | head -n1`
cd $LATTEST_OOZIE

./submit-to-oozie.sh dc-train-model/ pdendek hadoop-master dc-train-model/cluster.properties 
cd ..
#rm -r -f oozie_*
