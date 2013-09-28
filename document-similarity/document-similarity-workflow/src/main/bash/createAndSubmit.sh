#!/bin/bash

START_POS=`pwd`
cd ../../../ 
mvn clean install -DjobPackage -DskipTests

#cd target/oozie-wf/lib
#cp /usr/lib/hbase/lib/zookeeper.jar .
#cp /usr/lib/hbase/hbase-*-cdh4.*-security.jar .
#cp /usr/lib/hbase/lib/guava-11.0.2.jar .

USER=$1
if [ "$USER" == "" ] ; then 
 USER=pdendek
 echo "setting USER to default value (pdendek)"
fi

cd $START_POS 
echo $START_POS

./copy-to-oozie.sh ${USER}

echo "Submiting workflow to Oozzie Server: hadoop-master.vls.icm.edu.pl:11000"
oozie job -oozie http://hadoop-master:11000/oozie -config ../../../target/oozie-wf/cluster.properties -run
