#!/bin/sh

hadoop fs -text $1 | hadoop fs -put - $2