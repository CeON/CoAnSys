#!/bin/bash

eval "pig -x local -4 ./log4j.properties  docSim.pig"
