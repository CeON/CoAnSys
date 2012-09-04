#!/bin/bash

eval "cd ../..";
eval "mvn clean";
eval "mvn install";
eval "mvn assembly:single";
eval "mkdir src/pig/lib";
eval "cp target/document-classification-1.0-SNAPSHOT-jar-with-dependencies.jar src/pig/lib";
eval "cd src/pig";
eval "rm -r -f /tmp/docsim.pigout"
eval "pig -x local -4 ./log4j.properties  docSim.pig"
