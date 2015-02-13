#!/bin/sh

FILE=$1

ERRORLOG="error.txt"

echo "# Converting nt $FILE to insert"
echo $FILE >> $ERRORLOG

# check for errors
bzcat $FILE | rapper -c -i turtle -I - - file 2>&1 | grep -i "Error" >> $ERRORLOG


echo 'INSERT INTO "RAW_SAMEAS" ("URI_FreeBase", "URI_DBpedia") VALUES '
# get all sameas links
bzcat $FILE | grep "\#sameAs" |  rapper -i turtle -I - - file  2>/dev/null | cut -f1,3 -d '>' | sed 's/>//g' | sed 's/<//g' | sed 's/$/"),/' | sed 's/ /" , "/' | sed 's/^/("/' 

# Dummy triple for syntax reasons, it's a duplicate, so no harm done
echo '("http://dbpedia.org/resource/Zygophyllum_fabago" , "http://eunis.eea.europa.eu/species/184545") ;'

