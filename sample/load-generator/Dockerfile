FROM java:8

MAINTAINER Johannes Unterstein <junterstein@mesosphere.io>

ADD target/load-generator-1.0-SNAPSHOT-jar-with-dependencies.jar /app.jar
EXPOSE 8080

CMD ["java", "-jar", "/app.jar"]
