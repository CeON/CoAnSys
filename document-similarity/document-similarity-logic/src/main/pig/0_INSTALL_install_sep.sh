#!/bin/bash

#
# (C) 2010-2012 ICM UW. All rights reserved.
#

cd ../../..
mvn clean install -P sep -DskipTests
mkdir src/main/pig/lib
cp target/*.jar src/main/pig/lib
