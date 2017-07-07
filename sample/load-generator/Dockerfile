FROM ubuntu

MAINTAINER Johannes Unterstein <junterstein@mesosphere.io>

RUN apt-get update && apt-get install -y pwgen curl && apt-get clean && rm -r /var/lib/apt/lists
ADD app.sh /app.sh

CMD ["/app.sh"]
