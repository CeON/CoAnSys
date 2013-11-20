#!/bin/bash

shopt -s expand_aliases; 
source ~/.bashrc;
if ! type mvn3 > /dev/null  2>&1; then
  alias mvn3=mvn
fi

cd ../../../..
mvn3 clean install -P sep -DskipTests
mkdir src/main/pig/disambiguation/lib
cp target/disambiguation-author-*-SNAPSHOT.jar src/main/disambiguation/lib
