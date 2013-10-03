#!/bin/bash

START_POS=`pwd`
./install.sh

USER=$1
if [ "$USER" == "" ] ; then 
 USER=pdendek
 echo "setting USER to default value (pdendek)"
fi

<<OUT_OUT
cd $START_POS 
echo $START_POS
OUT_OUT

./copy-to-oozie.sh ${USER}

