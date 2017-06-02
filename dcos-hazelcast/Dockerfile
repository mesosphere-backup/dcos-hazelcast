FROM java:8

MAINTAINER Johannes Unterstein <junterstein@mesosphere.io>

RUN apt-get update && apt-get install -y dnsutils && apt-get clean && rm -r /var/lib/apt/lists

ADD target/dcos-hazelcast-1.0-SNAPSHOT.jar /app.jar
EXPOSE 5701

COPY app.sh /app.sh
CMD ["/app.sh"]
