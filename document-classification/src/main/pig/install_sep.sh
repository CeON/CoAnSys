#!/bin/bash

eval "cd ../../..";
eval "mvn clean install -P sep";
eval "mkdir src/main/pig/lib";
eval "cp target/document-classification-1.0-SNAPSHOT.jar src/main/pig/lib";
eval "cp target/document-classification-1.0-SNAPSHOT-only-dependencies.jar src/main/pig/lib";
eval "cd src/main/pig";
