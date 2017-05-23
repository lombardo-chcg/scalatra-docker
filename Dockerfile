FROM openjdk:8

ADD /target/scala-2.12/demo-api-assembly-0.1.0-SNAPSHOT.jar /usr/local/bin/demo-api-assembly-0.1.0-SNAPSHOT.jar
ADD /scripts/init.sh /usr/local/bin/init.sh
ADD /db/word_list.txt /db/word_list.txt

RUN chmod a+x /usr/local/bin/init.sh
#RUN apt-get update && apt-get install -y postgresql-client

EXPOSE 8080

ENTRYPOINT ["/usr/local/bin/init.sh"]
