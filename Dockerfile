FROM openjdk:8

ADD ./target/scala-2.12/demo-api-assembly-0.1.0-SNAPSHOT.jar /usr/local/bin/demo-api-assembly-0.1.0-SNAPSHOT.jar
ADD /scripts/init.sh /usr/local/bin/init.sh

RUN chmod a+x /usr/local/bin/init.sh

EXPOSE 8080

ENTRYPOINT ["/usr/local/bin/init.sh"]
