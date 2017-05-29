FROM java:8

MAINTAINER Johannes Unterstein <junterstein@mesosphere.io>

ADD target/marathon-hazelcast-1.0-SNAPSHOT-jar-with-dependencies.jar /app.jar
EXPOSE 5701

CMD ["java", "-jar", "/app.jar"]
