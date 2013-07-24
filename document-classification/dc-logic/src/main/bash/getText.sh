#!/bin/sh
hadoop fs -get $1/$2
echo modelPath `cat ./$2/$3`
