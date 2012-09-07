#!/bin/bash

TASK=$1
TASK_ID=$2
USER=$3
OOZIE_SERVER=$4
PROPERTIES_FILE=$5

WORKFLOW_HDFS_DIR="/user/${USER}/workflows/coansys/${TASK}-${TASK_ID}"

# echo "Copying required libaries to ${TASK}/lib"
# sudo -u ${USER} rm ${TASK}/lib/*
# sudo -u ${USER} cp ../../../../commons/target/commons-1.0-SNAPSHOT-jar-with-dependencies.jar  ${TASK}/lib/
# sudo -u ${USER} cp ../../../../importers/target/importers-1.0-SNAPSHOT-jar-with-dependencies.jar ${TASK}/lib/

echo "Recreating workflow data in HDFS"
sudo -u ${USER} hadoop fs -rm -r ${WORKFLOW_HDFS_DIR}
sudo -u ${USER} hadoop fs -mkdir ${WORKFLOW_HDFS_DIR}
echo "Putting current workflow data to HDFS"
sudo -u ${USER} hadoop fs -put ${TASK}/* ${WORKFLOW_HDFS_DIR}
echo "Submiting workflow to Oozzie Server: ${OOZIE_SERVER}:11000"
sudo -u ${USER} oozie job -oozie http://${OOZIE_SERVER}:11000/oozie -config ${PROPERTIES_FILE} -run
