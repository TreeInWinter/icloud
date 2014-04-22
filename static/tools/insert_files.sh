#!/bin/bash

cd ~/travelzen/proto-b2b/

git status | grep "modified" | awk -F: '{print $2}' > $$.txt

find 运营商/ -name "*.html" > $$.txt

for filename in `cat  $$.txt`
do

    mv "${filename}" "${filename}.bak"
    echo "inserting file ${filename}"
    sed -f ./tools/insert.sed "${filename}.bak" > "${filename}"

	if [ $? = 0 ]; then
		echo "replaceing ${filename} done"
	else
		echo "replaceing ${filename} error"
	fi
done

find . -type f -name "*.bak" | xargs rm -rf
rm -rf $$.txt
