#!/bin/bash


find ../运营商/ -type f -name "*.html" | xargs grep -E "kendo\.default\.css|kendo\.uniform\.css" | awk -F: '{print $1}' > tmp.txt

cat tmp.txt | sort -u > $$.txt

for filename in `cat  $$.txt`
do
	echo "replaceing $filename"
	mv  $filename ${filename}.bak
	#cat ${filename}.bak | sed -e 's/"\/js/"\/static\/js/g' -e 's/"\/css/"\/static\/css/g' -e 's/http:\/\/192.168.160.172\/static\//\/static\//g' -e 's/http:\/\/ajax.googleapis.com\/ajax\/libs\/jquery\/1\//\/static\/js\//g' > $filename 
	cat ${filename}.bak | sed -e 's/kendo\.default\.css/kendo\.gray\.css/g' -e 's/kendo\.uniform\.css/kendo\.gray\.css/g' > $filename 
	
	if [ $? = 0 ]; then

		echo "replaceing $filename done"
	else
		echo "replaceing $filename error"
	fi
done

find . -type f -name "*.bak" | xargs rm -rf

rm -rf $$.txt

