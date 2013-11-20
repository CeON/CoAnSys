#!/bin/bash

USER=$1
if [ "$USER" == "" ] ; then
 USER=pdendek
 echo "setting USER to default value (pdendek)"
fi

./copy-to-oozie.sh ${USER}

echo "Submiting workflow to Oozzie Server: hadoop-master.vls.icm.edu.pl:11000"
oozie job -oozie http://hadoop-master.vls.icm.edu.pl:11000/oozie -config ../../../target/oozie-wf/cluster.properties -run
