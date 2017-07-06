#!/bin/bash

cd $(dirname $0)

docker build --tag unterstein/dcos-hazelcast-sample:latest .
