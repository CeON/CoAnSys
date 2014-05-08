#! /usr/bin/python

import sys,re,string,difflib
sys.path.append("/home/pdendek/CoAnSys-build/document-similarity/document-similarity-logic/target/document-similarity-logic-1.6-SNAPSHOT-jar-with-dependencies.jar")
sys.path.append("/home/pdendek/CoAnSys-build/keywords-extraction/keywords-extraction-impl/target/keywords-extraction-impl-1.6-SNAPSHOT-jar-with-dependencies.jar")
from pl.edu.icm.coansys.commons.java import DiacriticsRemover
from pl.edu.icm.coansys.kwdextraction.langident import LanguageIdentifierBean


def filterDiacritics(t):
 r = DiacriticsRemover.removeDiacritics(t).lower() #remove diacritics, .toLowerCase()
 r = re.sub('(\\w)[^a-zA-Z0-9\\s'+re.escape('!"#$%&\'()*+,-./:;<=>?@[\\]^_`{|}~')+']+(\\w)',r'\1\2',r) # remove weired signs within a string
 r = re.sub(r'[^a-zA-Z0-9_\-]+',' ',r) # remove stand-alone weired signs 
 r = r.strip()
 return r

"""
@outputSchema("t:(easyText:chararray)")
def simplifyText2(ti,ctgs):
 print ti
 print ctgs
 if ctgs is not None:
  unpacked = [filterDiacritics(ctg[0]) for ctg in ctgs]
 else:
  unpacked = ['']
 tokens = [filterDiacritics(w) for w in ti.split()]
 joined = sorted(unpacked + tokens)
 return ''.join(joined)
"""
"""
@outputSchema("t:(easyText:chararray)")
def loremIpsum(ti,ctgs):
 if ctgs is not None:
  unpacked = [ctg[0] for ctg in ctgs]
 else:
  unpacked = ['']
 tokens = [w for w in ti.split()]
 joined = sorted(unpacked + tokens)
 return ''.join(joined)
"""

@outputSchema("t:(levenstein:double)")
def levenstein(t1,t2):
 a = difflib.SequenceMatcher(None, t1, t2)
 return str(a.ratio())


@outputSchema("t:(output:chararray)")
def donothing(t1):
 return t1

@outputSchema("b:{(tfidf:double)}")
def sortBagDesc(t1):
 return sorted(t1,reverse=True)

@outputSchema("b:{(tfidf:double)}")
def sortBagAsc(t1):
 return sorted(t1)

@outputSchema("t:(val:double)")
def roundNum(t1,precision):
 return round(t1,int(precision))

@outputSchema("t:(val:chararray)")
def roundNumCast(t1,precision):
 return str(round(t1,int(precision)))
