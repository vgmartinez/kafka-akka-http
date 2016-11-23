# Pull base image
FROM dev.docker.kickstartteam.es:5000/kst/scala/kerberos:5-onbuild

COPY src/main/resources/kafka_jaas.conf /usr/src/app/kafka_jaas.conf

ENV JAVA_OPTS="-Djava.security.auth.login.config=/usr/src/app/kafka_jaas.conf -Djavax.security.auth.useSubjectCredsOnly=false"

EXPOSE 9002
