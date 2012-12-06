#!/usr/bin/python
'''
 (C) 2010-2012 ICM UW. All rights reserved.
'''
import string;
import sys;
import re;
import collections;
import pickle

#create the dictionary of substitutions form given input file

def dictionaryCreation():
	myfile = open(sys.argv[1], "r");
	regex = re.compile(r'^@[^@ \t\n]+@');
	
	
	subs={}
	for line in myfile:
		found = regex.findall(line);
		opts_index=0
		
		if len(found) > 0:
			subs[line.split()[0]]=line.split()[1:]
	return subs


#create cross product of substitutions
def crossProductCreation(subs):
	a_key=[]
	aa_value=[]
	
	while len(subs) != 0 :
		new_key, new_a_value = subs.popitem()
		a_key.append(new_key)
		if len(aa_value)==0:
			for new_value in new_a_value:
				aa_value.append([new_value])
		else:
			next_aa_value=[]
			for a_value in aa_value:
					for new_value in new_a_value:
						next_a_value=list(a_value)
						next_a_value.append(new_value)
						next_aa_value.append(next_a_value)
			aa_value=next_aa_value
	return (a_key,aa_value)

def persist(a_key,aa_value):
	a_key, aa_value = crossProductCreation(dictionaryCreation())
	
	TMP=sys.argv[2]
	f_key = open(TMP+'./tmp_key.txt','w')
	pickle.dump(a_key,f_key)
	f_key.flush()
	f_key.close()
	
	f_val = open(TMP+'./tmp_val.txt','w')
	pickle.dump(aa_value,f_val)
	f_val.flush()
	f_val.close()
	
	for a_value in aa_value:
		print string.join(a_value, "_")

##############################################################
##############################################################
a_key,aa_value = crossProductCreation(dictionaryCreation())
persist(a_key,aa_value)
