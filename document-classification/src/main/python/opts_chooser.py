#!/usr/bin/python

import string;
import sys;
import re;
import collections;
import pickle

f_key = open(sys.argv[1],'rw')
a_key = pickle.load(f_key)
f_key.close()

a_val = sys.argv[2].split('_')

in_path=sys.argv[3]
name_from = sys.argv[4];
name_to = sys.argv[5];

out_path=string.replace(sys.argv[3],name_from,name_to)

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

f_in.close()
f_out.flush()
f_out.close()

print './'+out_path
