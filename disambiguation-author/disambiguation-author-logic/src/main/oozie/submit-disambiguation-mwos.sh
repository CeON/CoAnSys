#!/bin/sh
WHICH=$1

if [ "$WHICH" != "" ]
then
	WHICH="-${WHICH}"
fi

./submit-to-oozie.sh "new-author-disambiguation${WHICH}" mwos hadoop-master.vls.icm.edu.pl "new-author-disambiguation${WHICH}/cluster.properties"
