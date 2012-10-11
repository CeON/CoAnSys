#!/bin/bash

eval "mvn clean";
eval "mvn install";
eval "mvn assembly:single";
