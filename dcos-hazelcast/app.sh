#!/bin/bash

# calculating dns name for service discovery, see https://docs.mesosphere.com/1.8/usage/service-discovery/dns-overview/
parts=$(echo $MARATHON_APP_ID | tr "/" " ")

# join the array together again by `-` as separator
result=""
separator=""
for part in $parts
do
	result="$part$separator$result"
	separator="-"
done

url="$result.marathon.autoip.dcos.thisdcos.directory"
echo "URL using for service discovery: $url"

# start try to gather dns date
for i in {1..20}
do
	digs=`dig +short $url`
	if [ -z "$digs" ]; then
		echo "no DNS record found for $url"
	else
		# calculate discovery members
		echo "calculated initial discovery members: $digs"
		export HAZELCAST_INITIAL_MEMBERS=$digs
		break
	fi
   sleep 2
done

java -jar /app.jar
