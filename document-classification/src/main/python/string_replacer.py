#!/usr/bin/python

import string;
import sys;
import re;
import collections;
import pickle

#create the dictionary of substitutions form given input file

param = sys.argv[2];
val = sys.argv[3];

name_from = sys.argv[4];
name_to = sys.argv[5];

in_path = sys.argv[1];
out_path = re.sub(r''+name_from+'',name_to+'',sys.argv[1])

in_file = open(in_path,'r')
out_file = open(out_path,'w')

for line in in_file:
	new=string.replace(line,param,val)
	out_file.write(new)

out_file.flush()
out_file.close()
in_file.close()

print './'+out_path
