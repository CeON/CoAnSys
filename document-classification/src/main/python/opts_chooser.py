#!/usr/bin/python

import string;
import sys;
import re;
import collections;
import pickle

f_key = open(sys.argv[1],'r')
a_key = pickle.load(f_key)
f_key.close()

a_val = sys.argv[2].split('_')

in_path=string.replace(sys.argv[3],'.ingridient','.better')
out_path=string.replace(sys.argv[3],'.ingridient','')

f_in = open(in_path,'r')
f_out = open(out_path,'w')

for line in f_in:
	for i in range(1,len(a_key)):
		key=a_key[i]
		if key in line:
			newkey=string.replace(key,'@','')
			f_out.write(newkey+'='+a_val[i])
		else:
			f_out.write(line)

