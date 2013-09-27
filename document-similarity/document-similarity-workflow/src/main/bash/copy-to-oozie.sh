#!/bin/bash

USER=$1

WORKFLOW_HDFS_DIR="/user/${USER}/workflows/pl.edu.icm.coansys-document-similarity-workflow"

echo "Recreating workflow data in HDFS"
hadoop fs -rm -r ${WORKFLOW_HDFS_DIR}
hadoop fs -mkdir ${WORKFLOW_HDFS_DIR}
echo "Putting current workflow data to HDFS"
hadoop fs -put ls ../../../target/oozie-wf/* ${WORKFLOW_HDFS_DIR}
