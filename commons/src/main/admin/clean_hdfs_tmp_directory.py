#!/usr/bin/env python

import subprocess, datetime, re, tempfile

DIRECTORYTOCLEAN="/tmp"
OLDERTHAN=30 # days


def upOneLevel(path):
    if path.endswith("/"):
        path = path[:-1]
    return "/" + "/".join(path.split("/")[1:-1])


def isTooOld(path, datesMap):
    dateStr = datesMap[path]
    del(datesMap[path])
    now = datetime.datetime.now()
    fileTime = datetime.datetime.strptime(dateStr, '%Y-%m-%d')
    delta = now - fileTime
    return delta.days > OLDERTHAN


if __name__ == "__main__":

    listingfile = tempfile.NamedTemporaryFile()
    datesMap = {}

    subprocess.call("hadoop fs -ls -R " + DIRECTORYTOCLEAN + " > " + listingfile.name, shell=True)


    lastdir = DIRECTORYTOCLEAN
    for line in listingfile:

        m = re.search('^(?P<perm>[drwxsStT-]{10})( +[^ ]+){4} (?P<date>[^ ]+) [^ ]+ (?P<path>.*)$', line)
        perm = m.group('perm')
        date = m.group('date')
        path = m.group('path')

        isDirectory = perm[0] == 'd'

        datesMap[path] = date

        if not isDirectory and isTooOld(path, datesMap):
            print "deleting file " + path
            subprocess.call("hadoop fs -rm '" + path + "'", shell=True)
    
        while not path.startswith(lastdir):
            if isTooOld(lastdir, datesMap):
                print "deleting directory " + lastdir
                subprocess.call("hadoop fs -rmdir '" + lastdir + "'", shell=True)
            lastdir = upOneLevel(lastdir)
    
        if isDirectory:
            lastdir = path
    
    while lastdir != DIRECTORYTOCLEAN:
        if isTooOld(lastdir, datesMap):
            print "deleting directory " + lastdir
            subprocess.call("hadoop fs -rmdir '" + lastdir + "'", shell=True)
        lastdir = upOneLevel(lastdir)
    
