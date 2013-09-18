#!/bin/sh

TASK="oozie"
USER=mwos
OOZIE_SERVER=hadoop-master.vls.icm.edu.pl
PROPERTIES_FILE=oozie/cluster.properties

./submit-to-oozie.sh "${TASK}" "${USER}" "${OOZIE_SERVER}" "${PROPERTIES_FILE}"
