grep -H "Copyright (c) 2012-2013 ICM-UW" -R | cut -f1 -d: | while read -r line ; do sed -i "s/2012-2013 ICM-UW/2012-2015 ICM-UW/g" ${line}; done;
grep -H "Copyright (c) 2012-2014 ICM-UW" -R | cut -f1 -d: | while read -r line ; do sed -i "s/2012-2014 ICM-UW/2012-2015 ICM-UW/g" ${line}; done;

find . -name "*.scala" -exec grep -L "Copyright (c) 2012-2015 ICM-UW" {} \; | while read -r line; do cat boilerplate.java ${line} > ${line}.new; done;
find . -name "*.new" -print | while read -r line; do X=`echo "$line" | rev | cut -d. -f2- | rev`; mv $line $X ; done;
git status | grep "modified" | rev | cut -f1 -d: | rev | while read -r line; do git add ${line}; done;
