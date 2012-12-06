#!/bin/bash

TASK=$1
USER=$2
OOZIE_SERVER=$3
PROPERTIES_FILE=$4

./copy-to-oozie.sh ${TASK} ${USER}

echo "Submiting workflow to Oozzie Server: ${OOZIE_SERVER}:11000"
oozie job -oozie http://${OOZIE_SERVER}:11000/oozie -config ${PROPERTIES_FILE} -run
