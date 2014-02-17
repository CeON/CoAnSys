#!/bin/bash

# This file is part of CoAnSys project.
# Copyright (c) 2012-2013 ICM-UW
# 
# CoAnSys is free software: you can redistribute it and/or modify
# it under the terms of the GNU Affero General Public License as published by
# the Free Software Foundation, either version 3 of the License, or
# (at your option) any later version.
#
# CoAnSys is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
# GNU Affero General Public License for more details.
# 
# You should have received a copy of the GNU Affero General Public License
# along with CoAnSys. If not, see <http://www.gnu.org/licenses/>.

# author: Artur Czeczko <a.czeczko@icm.edu.pl>



# This script is a 2nd step of creating test set of titles pairs 
# for the deduplication document module. 
# 
# In the 1st step, a debug file of deduplication document must be created, 
# using special debug mode of the deduplication document module: 
# 
#   hadoop jar <path_to_deduplication_document_jar> \
#       pl.edu.icm.coansys.deduplication.document.DuplicateWorkDetector \
#       -D DEDUPLICATION_DEBUG_MODE=true \
#       <input_file> <output_dir>
# 
# The second step separates false positives and true positives from the 
# output of the 1st step, using this script:
# 
#   ./separate_duplicates.sh <debug_file> 1.0
#   ./separate_duplicates.sh <debug_file> 0.9
#   ./separate_duplicates.sh <debug_file> 0.8
#   ./separate_duplicates.sh <debug_file> 0.7
# 
# User have to decide which pairs are true or false positives.
# 
# In the 3rd step a pig script make_titles_test_set.pig must be used to 
# extract real titles pairs (not normalized) from the input data.



if [ "$#" -lt 2 ]; then
    echo "Usage: $0 <input_file> <voter_result>" > &2
    echo "e.g. $0 deduplication-debug.txt 0.8" > &2
fi

INPUT_FILE=$1
VOTER_RESULT=$2 # e.g. 0.7


REAL_DUPLICATES_FILE="real_duplicates_$VOTER_RESULT"
FALSE_DUPLICATES_FILE="false_duplicates_$VOTER_RESULT"
NOT_SURE_FILE="not_sure_$VOTER_RESULT"
GREP_PATTERN="TitleVoter:PROBABILITY-$VOTER_RESULT"


function decide {
    res=`echo "$1" | \
    grep "$GREP_PATTERN" | \
    cut -d '	' -f 2 | \
    cut -d '#' -f 1 | \
    sed 's/, /,/' | \
    awk -F ',' '$1!=$2 { print($1 "\n" $2 "\n"); }'`
    
    if [ -n "$res" ]; then
        echo "$res"
        echo -n "Is it duplicate? ([y]/n/?) "
        read -u 2 resp
        if [ "$resp" == "n" ]; then
            echo "$1" >> "$FALSE_DUPLICATES_FILE"
        elif [ "$resp" == "?" ]; then
            echo "$1" >> "$NOT_SURE_FILE"
        else
            echo "$1" >> "$REAL_DUPLICATES_FILE"
        fi
    fi
}


cat $INPUT_FILE | while read linia; do \
    decide "$linia"
done
