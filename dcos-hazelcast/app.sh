#!/bin/bash

echo "muh $MARATHON_APP_ID"
echo $HAZELCAST_MIN_CLUSTER_SIZE

url="hazelcast.marathon.containerip.dcos.thisdcos.directory"
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
