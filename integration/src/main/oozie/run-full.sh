#!/bin/bash

TASK=full
USER=$1
OOZIE_SERVER=$2
PROPERTIES_FILE=$3

WORKFLOW_HDFS_DIR="/user/${USER}/workflows/${TASK}"

echo "Copying subworkflow files"
CD=`pwd`
./copy-to-oozie.sh qep-load ${USER}
cd ../../../../importers/src/main/oozie/
./copy-to-oozie.sh importers-hbase-dump ${USER}
cd ../../../../document-similarity/src/main/oozie/
./copy-to-oozie.sh similarity ${USER}
cd ../../../../disambiguation-author/src/main/oozie/
./copy-to-oozie.sh disambiguation-author-hdfs ${USER}
./copy-to-oozie.sh coauthor-pairs ${USER}
cd ${CD}

echo "Recreating workflow data in HDFS"
sudo -u ${USER} hadoop fs -rm -r ${WORKFLOW_HDFS_DIR}
sudo -u ${USER} hadoop fs -mkdir ${WORKFLOW_HDFS_DIR}
echo "Putting current workflow data to HDFS"
sudo -u ${USER} hadoop fs -put ${TASK}/* ${WORKFLOW_HDFS_DIR}
echo "Submiting workflow to Oozzie Server: ${OOZIE_SERVER}:11000"
sudo -u ${USER} oozie job -oozie http://${OOZIE_SERVER}:11000/oozie -config ${PROPERTIES_FILE} -run
