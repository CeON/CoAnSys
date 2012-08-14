#!/bin/bash

WORKFLOW=$1
USER=$2
OOZIE_SERVER=$3
echo "Copying required libaries to ${WORKFLOW}/lib"
sudo -u ${USER} cp ../../../../commons/target/commons-1.0-SNAPSHOT-jar-with-dependencies.jar  ${WORKFLOW}/lib/
sudo -u ${USER} cp ../../../../importers/target/importers-1.0-SNAPSHOT-jar-with-dependencies.jar ${WORKFLOW}/lib/

sudo -u ${USER} hadoop fs -mkdir /user/${USER}/coansys
echo "Removing old workflow data from HDFS"
sudo -u ${USER} hadoop fs -rm -r "/user/${USER}/coansys/${WORKFLOW}"
echo "Putting current workflow data to HDFS"
sudo -u ${USER} hadoop fs -put "${WORKFLOW}" /user/${USER}/coansys
echo "Submiting workflow to Oozzie Server: ${OOZIE_SERVER}:11000"
sudo -u ${USER} oozie job -oozie http://${OOZIE_SERVER}:11000/oozie -config "${WORKFLOW}/job.properties" -run
