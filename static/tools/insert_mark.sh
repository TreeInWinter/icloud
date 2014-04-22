#!/bin/bash

cd ~/travelzen/proto-b2b/
find 运营商/ -type f -name "*.html" > tmp.txt

#cat tmp.txt | sort -u > $$.txt
for filename in `cat  tmp.txt`
do
	echo "checking $filename"
    match_count=`cat "${filename}" | grep -c 'IE7\.min\.js'` 
    
	if [ $match_count = 0 ]; then
        mv "${filename}" "${filename}".bak
        sed -e 's/<\/head>/<!-- @CLB@ --><\/head>/' "${filename}.bak" > "${filename}"
        #\
        #    -e 's/<!-- @CLB @-->/<!--\[if lt IE 8\]><link rel="stylesheet" href="\/static\/css\/ie\.css" \/><script type="text\/javascript" src="\/static\/js\/vendor\/IE7\.min\.js"><\/script><!\[endif\]-->/' "${filename}".bak > "${filename}"
	fi

	if [ $? = 0 ]; then
		echo "replaceing ${filename} done"
	else
		echo "replaceing ${filename} error"
	fi
done

find . -type f -name "*.bak" | xargs rm -rf
rm -rf tmp.txt
echo "complete"
exit 0
