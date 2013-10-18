#!/bin/bash

#
# (C) 2010-2012 ICM UW. All rights reserved.
#

CURR=`pwd`
cd ../../../..
mvn clean install -P sep
mkdir src/main/pig/lib
cp target/*-SNAPSHOT*.jar src/main/pig/lib
