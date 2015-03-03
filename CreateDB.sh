#!/bin/bash

while read line
do
    curl $line | bzcat | grep "\#sameAs" |  rapper -i ntriples -I - - file  2>/dev/null | cut -f1,3 -d '>' | sed 's/> </\t/g' | sed 's/> .&//g' | sed 's/^<//g' | sort -u
done < links.txt > ld.tsv

DBUSER=root
DBPASSWORD=aux123
DBNAME=dbSameAs
DBSERVER=localhost

DBCONN="-h ${DBSERVER} -u ${DBUSER} --password=${DBPASSWORD}"

echo "DROP DATABASE IF EXISTS ${DBNAME}" | mysql ${DBCONN}
echo "CREATE DATABASE ${DBNAME}" | mysql ${DBCONN}
echo "CREATE TABLE RAW_SAMEAS (dbpedia_uri varchar(750) DEFAULT NULL, link_target varchar(750) DEFAULT NULL)" | mysql $DBCONN $DBNAME

echo "LOAD DATA LOCAL INFILE 'ld.tsv' INTO TABLE RAW_SAMEAS FIELDS TERMINATED BY '\t'" | mysql $DBCONN $DBNAME --local-infile=1
