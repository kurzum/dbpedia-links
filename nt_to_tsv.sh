#!/bin/sh

for i in `ls *.bz2` 
do 
	FILE=$i

	ERRORLOG="error.txt"

	echo $FILE >> $ERRORLOG

	# check for errors
	bzcat $FILE | rapper -c -i ntriples -I - - file 2>&1 | grep -i "Error" >> $ERRORLOG


	
	# get all sameas links
	bzcat $FILE | grep "\#sameAs" |  rapper -i ntriples -I - - file  2>/dev/null | cut -f1,3 -d '>' | sed 's/> </\t/g' | sed 's/> .&//g' | sed 's/^<//g' 

done
