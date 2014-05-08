#!/usr/bin/python

import sys

@outputSchema("v:chararray")
def roundV(val,length):
 r = round(val, length)
 r = str(r)
 return r

