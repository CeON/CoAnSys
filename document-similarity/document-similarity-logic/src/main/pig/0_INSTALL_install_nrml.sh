#!/bin/bash

#
# (C) 2010-2012 ICM UW. All rights reserved.
#

cd ../../..
mvn clean install -D skipTests
mkdir src/main/pig/lib
cp target/*SNAPSHOT.jar src/main/pig/lib
