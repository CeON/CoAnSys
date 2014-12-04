#! /usr/bin/python

import sys,re,string
sys.path.append("/home/pdendek/docsim-check/out/document-similarity-logic-1.6-SNAPSHOT-jar-with-dependencies.jar")
from pl.edu.icm.coansys.commons.java import DiacriticsRemover

def fillDict(inL,langs):
 innerD = {}
 for x in inL:
  t,l = x
  langs.add(l)
  innerD[l] = t
 return (innerD,langs)

def replaceInList(l,old,new):
 try:
  l.remove(old)
  l.append(new)
 except ValueError:
  pass
 return l

def replaceKInDict(d,old,new):
 try:
  d[new] = d.pop(old)
 except KeyError:
  pass
 return d

def filterDiacritics(t):
 r = DiacriticsRemover.removeDiacritics(t).lower() #remove diacritics, .toLowerCase()
 r = re.sub('(\\w)[^a-zA-Z0-9\\s'+re.escape('!"#$%&\'()*+,-./:;<=>?@[\\]^_`{|}~')+']+(\\w)',r'\1\2',r) # remove weired signs within a string
 r = re.sub(r'[^a-zA-Z0-9_\-]+',' ',r) # remove stand-alone weired signs 
 r = r.strip()
 return r

def getFilteredItemOrNone(d,k):
 r = None
 try:
  r = filterDiacritics(d[k])
 except KeyError:
  pass
 return r

@outputSchema("b:{ t:( key:chararray, title:chararray, abstract:chararray, ccs:{ cc:( type:chararray, code:chararray) }, lang:chararray ) }")
def groupByLangAndFilter(key,tis,abstrs,ccs):
 langs = set()
 unpacked_ccs = [(cc[0],thiscc[0]) for cc in ccs for thiscc in cc[1]]
 ltis,langs = fillDict(tis,langs)
 labstrs,langs = fillDict(abstrs,langs)
 langs = replaceInList(list(langs),u'',u'und')
 ltis = replaceKInDict(ltis,u'',u'und')
 labstrs = replaceKInDict(labstrs,u'',u'und')
 out = [(key+u'-----'+unicode(lang),getFilteredItemOrNone(ltis,lang),getFilteredItemOrNone(labstrs,lang),unpacked_ccs,lang) for lang in langs]
 return out


@outputSchema("b:{ t:( key:chararray, title:chararray, abstract:chararray, ccs:{ cc:( type:chararray, code:chararray) }, lang:chararray ) }")
def groupByLangAndFilter(key,tis,abstrs,ccs,startsWith):
 langs = set()
 unpacked_ccs = [(cc[0],thiscc[0]) for cc in ccs if cc[0].startswith(startsWith) for thiscc in cc[1] ]
 ltis,langs = fillDict(tis,langs)
 labstrs,langs = fillDict(abstrs,langs)
 langs = replaceInList(list(langs),u'',u'und')
 ltis = replaceKInDict(ltis,u'',u'und')
 labstrs = replaceKInDict(labstrs,u'',u'und')
 out = [(key+u'-----'+unicode(lang),getFilteredItemOrNone(ltis,lang),getFilteredItemOrNone(labstrs,lang),unpacked_ccs,lang) for lang in langs]
 return out

@outputSchema("t:( key:chararray, text:chararray, ccs:{ cc:( type:chararray, code:chararray) } )")
def mergeAndFilter(key,tis,abstrs,ccs):
 unpacked_ccs = [(cc[0],thiscc[0]) for cc in ccs for thiscc in cc[1]]
 return (key,' '.join([filterDiacritics(o[0]) for o in tis+abstrs]), unpacked_ccs)

@outputSchema("t:( key:chararray, text:chararray, ccs:{ cc:( type:chararray, code:chararray) } )")
def mergeAndFilter(key,tis,abstrs,ccs,startsWith):
 unpacked_ccs = [(cc[0],thiscc[0]) for cc in ccs if cc[0].startswith(startsWith) for thiscc in cc[1]]
 t = ' '.join([o[0] for o in tis+abstrs])
 t = t.lower()
 return (key,' '.join([filterDiacritics(o[0]) for o in tis+abstrs]), unpacked_ccs)


