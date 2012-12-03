#!/bin/bash

rm -r tmp*
pig -x local sequencefile-loader.pig 
