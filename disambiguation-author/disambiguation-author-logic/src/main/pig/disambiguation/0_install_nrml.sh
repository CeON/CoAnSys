#!/bin/bash

cd ../../../..
mvn clean install -DskipTests
mkdir -p src/main/pig/disambiguation/lib
cp target/disambiguation-author-*-SNAPSHOT.jar src/main/pig/disambiguation/lib
