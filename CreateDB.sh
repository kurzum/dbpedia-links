#!/bin/bash

sh ./nt_to_tsv.sh > file2.tsv

DBUSER=root
DBPASSWORD=aux123
DBNAME=dbSameAs
DBSERVER=localhost

DBCONN="-h ${DBSERVER} -u ${DBUSER} --password=${DBPASSWORD}"

echo "DROP DATABASE IF EXISTS ${DBNAME}" | mysql ${DBCONN}
echo "CREATE DATABASE ${DBNAME}" | mysql ${DBCONN}
echo "CREATE TABLE TEMPDUP (dbpedia_uri varchar(750) DEFAULT NULL, link_target varchar(750) DEFAULT NULL)" | mysql $DBCONN $DBNAME

echo "LOAD DATA LOCAL INFILE 'file2.tsv' INTO TABLE TEMPDUP FIELDS TERMINATED BY '\t'" | mysql $DBCONN $DBNAME --local-infile=1

echo "CREATE TABLE RAW_SAMEAS as (Select Distinct dbpedia_uri,link_target from TEMPDUP)" | mysql $DBCONN $DBNAME

echo "DROP TABLE TEMPDUP" | mysql $DBCONN $DBNAME
