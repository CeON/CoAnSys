#!/bin/bash
cd ../../../..
mvn clean install -DskipTests -P sep
mkdir -p src/main/pig/mergeOrcid/lib
cp target/disambiguation-author-*-SNAPSHOT*.jar src/main/pig/mergeOrcid/lib
