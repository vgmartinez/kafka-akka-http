# Pull base image
FROM dev.docker.kickstartteam.es:5000/kst/scala/kerberos:5-onbuild

COPY src/main/resources/kafka_jaas.conf /usr/src/app/kafka_jaas.conf
COPY src/main/resources/ingestion.keytab /usr/src/app/ingestion.keytab
COPY src/main/resources/krb5.conf /etc/krb5.conf

ENV JAVA_OPTS="-Djava.security.auth.login.config=/usr/src/app/kafka_jaas.conf -Djavax.security.auth.useSubjectCredsOnly=false -Dsun.security.krb5.debug=true -Djavax.net.debug=all"
ENV REALM="KST.INTERNAL"
ENV KDC_ADDRESS="primarydns.kst.internal"
ENV KDC_PORT=88

EXPOSE 9002
