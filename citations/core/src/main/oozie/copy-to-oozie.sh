#!/bin/bash

TASK=$1
USER=$2

WORKFLOW_HDFS_DIR="/user/${USER}/workflows/${TASK}"
WORKFLOW_LOCAL_LIB_DIR=${TASK}/workflow/lib
WORKFLOW_LOCAL_BASH_DIR=${TASK}/workflow/bash

if [ ! -d "$WORKFLOW_LOCAL_LIB_DIR" ]; then
    mkdir ${WORKFLOW_LOCAL_LIB_DIR}
fi

if [ ! -d "$WORKFLOW_LOCAL_BASH_DIR" ]; then
    mkdir ${WORKFLOW_LOCAL_BASH_DIR}
fi

echo "Copying required scripts to ${TASK}"
cp ../../../../importers/src/main/bash/bulkload-sequence-file.sh ${WORKFLOW_LOCAL_BASH_DIR}

echo "Copying required libaries to ${TASK}/lib"
cp ../../../target/citations-1.0-SNAPSHOT-jar-with-dependencies.jar ${WORKFLOW_LOCAL_LIB_DIR}
cp /usr/lib/hbase/hbase-0.92.1-cdh4.0.1-security.jar ${WORKFLOW_LOCAL_LIB_DIR}

echo "Recreating workflow data in HDFS"
hadoop fs -rm -r ${WORKFLOW_HDFS_DIR}
hadoop fs -mkdir ${WORKFLOW_HDFS_DIR}
echo "Putting current workflow data to HDFS"
hadoop fs -put ${TASK}/* ${WORKFLOW_HDFS_DIR}
