#!/bin/bash

for i in "$@"
do
	echo "disable '${i}'" | hbase shell
	echo "drop '${i}'" | hbase shell
done
