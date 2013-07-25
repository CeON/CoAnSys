#!/bin/bash

TASK=$1
USER=$2

WORKFLOW_HDFS_DIR="/user/${USER}/workflows/${TASK}"
WORKFLOW_LOCAL_LIB_DIR=${TASK}/workflow/lib/

if [ ! -d "$WORKFLOW_LOCAL_LIB_DIR" ]; then
    mkdir ${WORKFLOW_LOCAL_LIB_DIR}
fi


echo "Copying required libaries to ${TASK}/lib"
cp /usr/lib/pig/pig-*-cdh*.jar ${WORKFLOW_LOCAL_LIB_DIR}
cp ../../../../commons/target/commons-*-SNAPSHOT.jar  ${WORKFLOW_LOCAL_LIB_DIR}
cp ../../../../importers/target/importers-*-SNAPSHOT.jar ${WORKFLOW_LOCAL_LIB_DIR}
cp ../../../../disambiguation-author/target/disambiguation-author-*-SNAPSHOT*.jar ${WORKFLOW_LOCAL_LIB_DIR}
cp ../../../../disambiguation/target/disambiguation-*-SNAPSHOT.jar ${WORKFLOW_LOCAL_LIB_DIR}

echo "Copying required pig scripts to ${TASK}"
cp ../pig/*.pig  ${TASK}/workflow/

echo "Recreating workflow data in HDFS"
hadoop fs -rm -r ${WORKFLOW_HDFS_DIR}
hadoop fs -mkdir ${WORKFLOW_HDFS_DIR}
echo "Putting current workflow data to HDFS"
hadoop fs -put ${TASK}/* ${WORKFLOW_HDFS_DIR}
