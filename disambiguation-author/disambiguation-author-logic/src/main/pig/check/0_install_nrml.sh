#!/bin/bash

LIBPATH="`pwd`/lib/"
cd ../../../..
mvn clean install -P sep -DskipTests
mkdir -p $LIBPATH
cp target/disambiguation-author-*-SNAPSHOT.jar $LIBPATH

