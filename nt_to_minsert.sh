#!/bin/sh

for i in `ls *.bz2` 
do 
	FILE=$i
	COUNTER=0
	ERRORLOG="error.txt"

	echo $FILE >> $ERRORLOG

	# check for errors
	bzcat $FILE | rapper -c -i ntriples -I - - file 2>&1 | grep -i "Error" >> $ERRORLOG

	# get all sameas links
	for line in $(bzcat $FILE | grep "\#sameAs" |  rapper -i ntriples -I - - file  2>/dev/null | cut -f1,3 -d '>' | sed 's/>//g' | sed 's/<//g' | sed 's/$/");/' | sed 's/ /", , "/' | sed 's/^/("/'); do 
	
		 
		if [ ! ${#line} -eq "1" ] 
		then
			if [ $COUNTER -eq "1" ]
			then
				echo $line
				COUNTER=$((0))
			else
				echo 'INSERT INTO RAW_SAMEAS (dbpedia_uri, link_target) VALUES ' $line
				COUNTER=$((COUNTER + 1))
			fi
		fi 
	done
done
