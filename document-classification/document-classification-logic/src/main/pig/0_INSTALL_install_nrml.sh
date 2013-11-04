#!/bin/bash

#
# (C) 2010-2012 ICM UW. All rights reserved.
#

cd ../../..
mvn clean install 
mkdir src/main/pig/lib
cp target/document-classification-*-SNAPSHOT*.jar src/main/pig/lib
