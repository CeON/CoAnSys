#!/bin/bash

cd ../../../..
mvn clean install -P sep -DskipTests
mkdir -p src/main/pig/disambiguation/lib
cp target/disambiguation-author-*-SNAPSHOT*.jar src/main/pig/disambiguation/lib
