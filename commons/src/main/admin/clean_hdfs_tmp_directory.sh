#!/bin/bash

DAYS=$1
DIFF_TIME=$((86400 * $DAYS))

hadoop fs -ls -R /tmp | awk -v diffTime="${DIFF_TIME}" '{filename=$8;date=$6;split(date,d,"-");dt=mktime(d[1]" "d[2]" "d[3]" 0 0 0");diff=systime()-dt;if (diff>diffTime) print filename }' | xargs hadoop fs -rm -r
