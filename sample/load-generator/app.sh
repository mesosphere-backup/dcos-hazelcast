#!/bin/bash
echo "Using this url to generate load: $VIP_APP"

for ((i=0; i<=AMOUNT; i++));
do
	echo "Run: $i"
	k="K$i"
	v=`pwgen 20`
	data="{\"key\": \"$k\",\"value\":\"$v\"}"
	echo $data
	curl -X POST -H 'Content-Type: application/json' $VIP_APP -d "$data"
	sleep 1
done