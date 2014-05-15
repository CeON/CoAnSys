#!/usr/bin/env python
 
import os
import sys
 
rank_crit = 0 # ranking column

last_val = None
rank = 0
interval = 1


for line in sys.stdin:
  lineX = line.strip()
  if len(lineX) == 0: 
    continue
  lineX = lineX[2:(len(lineX)-2)]
  for t in lineX.split('),('):
    tupleParts = t.split(',')
    curr_val = int(tupleParts[rank_crit].strip())
    if last_val != curr_val:
      rank = rank + interval
      interval = 1
      last_val = curr_val
      print '\t'.join([str(rank)]+tupleParts)
    else:
      print '\t'.join([str(rank)]+tupleParts)
      interval = interval + 1


