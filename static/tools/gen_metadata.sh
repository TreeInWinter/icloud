#!/bin/bash

if [ $# -lt 4 ]; then
    echo "Useage:./`basename $0` [pathname] [filesuffix] [keyword] [output filename]"
    exit 1 
fi

if [ ! -d "$1" ] ;then
    echo "$1 not found "
    exit 1
fi

cd $1

find . -type f -name "$2" | xargs grep "$3" |  sed -e 's/\n//g' -e 's/\r//g' -e 's/<title>//' -e 's/<\/title>//' -e 's/\${title!"天地行"}//' > $$.txt 
lineCount=`cat $$.txt | wc -l`

echo "var pageList = [ " > $4
cat $$.txt | awk -F: '{if (NR != totalLine) {printf("\"%s,%s\",\n",$1,$2)} \
    if (NR == totalLine) {printf("\"%s,%s\"\n",$1,$2)}}' totalLine=$lineCount >> $4
echo "]; " >> $4

rm -rf $$.txt
