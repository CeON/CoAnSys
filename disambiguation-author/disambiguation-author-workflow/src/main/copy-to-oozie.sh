#!/bin/bash

TASK=$1
USER=$2

WORKFLOW_HDFS_DIR="/user/${USER}/workflows/${TASK}"
WORKFLOW_LOCAL_LIB_DIR=oozie/lib/
WORKFLOW_LOCAL_PIG_DIR=oozie/pig/
LOCAL_PROJECT_PIG_DIR=../../../disambiguation-author-logic/src/main/pig

if [ ! -d "$WORKFLOW_LOCAL_LIB_DIR" ]; then
    mkdir ${WORKFLOW_LOCAL_LIB_DIR}
fi

if [ ! -d "$WORKFLOW_LOCAL_PIG_DIR" ]; then
    mkdir ${WORKFLOW_LOCAL_PIG_DIR}
fi

echo "Copying required libaries to ${TASK}/lib"
cp /usr/lib/pig/pig-*-cdh*.jar ${WORKFLOW_LOCAL_LIB_DIR}
#cp ../../../../commons/target/commons-*-SNAPSHOT.jar  ${WORKFLOW_LOCAL_LIB_DIR}
#cp ../../../../importers/target/importers-*-SNAPSHOT.jar ${WORKFLOW_LOCAL_LIB_DIR}
cp ../../../disambiguation-author-logic/target/disambiguation-author-*-SNAPSHOT*.jar ${WORKFLOW_LOCAL_LIB_DIR}
cp ../../../../disambiguation/target/disambiguation-*-SNAPSHOT.jar ${WORKFLOW_LOCAL_LIB_DIR}

echo "Copying required pig scripts to ${TASK}"
cp ${LOCAL_PROJECT_PIG_DIR}/disambiguation1.pig ${WORKFLOW_LOCAL_PIG_DIR}
cp ${LOCAL_PROJECT_PIG_DIR}/disambiguation100.pig ${WORKFLOW_LOCAL_PIG_DIR}
cp ${LOCAL_PROJECT_PIG_DIR}/disambiguation1000.pig ${WORKFLOW_LOCAL_PIG_DIR}
cp ${LOCAL_PROJECT_PIG_DIR}/spliter.pig ${WORKFLOW_LOCAL_PIG_DIR}

echo "Recreating workflow data in HDFS"
hadoop fs -rm -r ${WORKFLOW_HDFS_DIR}
hadoop fs -mkdir ${WORKFLOW_HDFS_DIR}
echo "Putting current workflow data to HDFS"
hadoop fs -put oozie/* ${WORKFLOW_HDFS_DIR}
