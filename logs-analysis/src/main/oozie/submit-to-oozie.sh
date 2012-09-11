#!/bin/bash

TASK=$1
TASK_ID=$2
USER=$3
OOZIE_SERVER=$4
PROPERTIES_FILE=$5

WORKFLOW_HDFS_DIR="/user/${USER}/workflows/coansys/${TASK}-${TASK_ID}"
WORKFLOW_LOCAL_LIB_DIR="${TASK}/workflow/lib"

echo "Copying required libaries to ${WORKFLOW_LOCAL_LIB_DIR}"
sudo -u "${USER}" rm ${WORKFLOW_LOCAL_LIB_DIR}/*
sudo -u "${USER}" cp ../../../../logs-analysis/target/logs-analysis-1.0-SNAPSHOT-jar-with-dependencies.jar  ${WORKFLOW_LOCAL_LIB_DIR}/

echo "Recreating workflow data in HDFS"
sudo -u "${USER}" hadoop fs -rm -r ${WORKFLOW_HDFS_DIR}
sudo -u "${USER}" hadoop fs -mkdir ${WORKFLOW_HDFS_DIR}
echo "Putting current workflow data to HDFS"
sudo -u "${USER}" hadoop fs -put ${TASK}/* ${WORKFLOW_HDFS_DIR}
echo "Submiting workflow to Oozzie Server: ${OOZIE_SERVER}:11000"
sudo -u "${USER}" oozie job -oozie http://${OOZIE_SERVER}:11000/oozie -config ${PROPERTIES_FILE} -run
