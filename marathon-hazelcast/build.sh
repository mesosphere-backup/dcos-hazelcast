#!/bin/bash

cd $(dirname $0)

docker build --tag unterstein/marathon-hazelcast:latest .
