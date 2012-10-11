#!/bin/bash

eval "cd ../../..";
eval "mvn install";
eval "mkdir src/main/pig/lib";
eval "cp target/document-classification-1.0-SNAPSHOT.jar src/main/pig/lib";
eval "cd src/main/pig";
