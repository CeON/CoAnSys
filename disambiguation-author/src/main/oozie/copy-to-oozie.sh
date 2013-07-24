#!/bin/bash

TASK=$1
USER=$2

WORKFLOW_HDFS_DIR="/user/${USER}/workflows/${TASK}"
WORKFLOW_LOCAL_LIB_DIR=${TASK}/workflow/lib/

if [ ! -d "$WORKFLOW_LOCAL_LIB_DIR" ]; then
    mkdir ${WORKFLOW_LOCAL_LIB_DIR}
fi

echo "Copying required libaries to ${TASK}/lib"
sudo -u ${USER} cp /usr/lib/pig/pig-*-cdh*.jar ${WORKFLOW_LOCAL_LIB_DIR}
sudo -u ${USER} cp ../../../../commons/target/commons-*-SNAPSHOT.jar  ${WORKFLOW_LOCAL_LIB_DIR}
sudo -u ${USER} cp ../../../../importers/target/importers-*-SNAPSHOT.jar ${WORKFLOW_LOCAL_LIB_DIR}
sudo -u ${USER} cp ../../../../disambiguation-author/target/disambiguation-author-*-SNAPSHOT.jar ${WORKFLOW_LOCAL_LIB_DIR}
sudo -u ${USER} cp ../../../../disambiguation/target/disambiguation-*-SNAPSHOT.jar ${WORKFLOW_LOCAL_LIB_DIR}

echo "Copying required pig scripts to ${TASK}"
sudo -u ${USER} cp ../pig/*.pig  ${TASK}/workflow/pig/

echo "Recreating workflow data in HDFS"
sudo -u ${USER} hadoop fs -rm -r ${WORKFLOW_HDFS_DIR}
sudo -u ${USER} hadoop fs -mkdir ${WORKFLOW_HDFS_DIR}
echo "Putting current workflow data to HDFS"
sudo -u ${USER} hadoop fs -put ${TASK}/* ${WORKFLOW_HDFS_DIR}
