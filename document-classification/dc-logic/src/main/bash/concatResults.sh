#!/bin/bash

#
# (C) 2010-2012 ICM UW. All rights reserved.
#

file="wyniki"
eval "rm -f $file" 
eval "touch $file"

eval "echo acc prec rec f1 hl zol >> ${file}" 

for i in `ls`
do
	if [ -d "$i" ]
	then	
		eval "echo ----- $i ----- >> ${file}"
		eval "cat wyniki ${i}/part-r-00000 > ${file}TMP"
		eval "rm ${file}"
		eval "mv ${file}TMP ${file}"
	fi
done

eval "cat ${file}"
