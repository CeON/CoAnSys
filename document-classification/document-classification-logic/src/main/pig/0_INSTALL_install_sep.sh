#!/bin/bash

#
# (C) 2010-2012 ICM UW. All rights reserved.
#

shopt -s expand_aliases; 
source ~/.bashrc;
if ! type mvn3 > /dev/null  2>&1; then
  alias mvn3=mvn
fi

cd ../../..
mvn3 clean install -P sep
mkdir src/main/pig/lib
cp target/document-classification-*-SNAPSHOT*.jar src/main/pig/lib
