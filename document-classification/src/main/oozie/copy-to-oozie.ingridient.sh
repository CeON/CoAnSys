#!/bin/bash

TASK=$1
USER=$2
RM=$3

if [ `hostname` = "localhost" ]; then
	WORKFLOW_HDFS_DIR="hdfs://localhost.localdomain:8020/user/${USER}/workflows/${TASK}/@TIME_INFIX@"
else
									WORKFLOW_HDFS_DIR="/user/${USER}/workflows/${TASK}/@TIME_INFIX@"	
fi

WORKFLOW_LOCAL_LIB_DIR=${TASK}/workflow/lib
WORKFLOW_LOCAL_PIG_DIR=${TASK}/workflow/pig

echo "Removing ${WORKFLOW_LOCAL_LIB_DIR} and ${WORKFLOW_LOCAL_PIG_DIR}"
if [ "${RM}" = "RM" ]; then
	rm -r -f ${WORKFLOW_LOCAL_LIB_DIR}
	rm -r -f ${WORKFLOW_LOCAL_PIG_DIR}
fi

if [ ! -d "$WORKFLOW_LOCAL_LIB_DIR" ]; then
    mkdir ${WORKFLOW_LOCAL_LIB_DIR}
fi

if [ ! -d "$WORKFLOW_LOCAL_PIG_DIR" ]; then
    mkdir ${WORKFLOW_LOCAL_PIG_DIR}
fi

echo "Copying required libaries to ${TASK}/lib"
cp /usr/lib/pig/pig-0.9.2-cdh4.0.1.jar ${WORKFLOW_LOCAL_LIB_DIR}
cp ../../../../document-classification/target/document-classification-1.0-SNAPSHOT.jar ${WORKFLOW_LOCAL_LIB_DIR}
cp ../../../../document-classification/target/document-classification-1.0-SNAPSHOT-only-dependencies.jar ${WORKFLOW_LOCAL_LIB_DIR}

echo "Copying required pig scripts to ${TASK}"
cp -r ../pig/*.pig ${WORKFLOW_LOCAL_PIG_DIR}

echo "Recreating workflow data in HDFS"
hadoop fs -rm -r ${WORKFLOW_HDFS_DIR}
hadoop fs -mkdir ${WORKFLOW_HDFS_DIR}
echo "Putting current workflow data to HDFS"
hadoop fs -put ${TASK}/* ${WORKFLOW_HDFS_DIR}
