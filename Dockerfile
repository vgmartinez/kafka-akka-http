#
# Scala and sbt Dockerfile
#

# Pull base image
FROM ubuntu:16.04

ENV SCALA_VERSION 2.11.8
ENV SBT_VERSION 0.13.13

# Install sbt
RUN \
  apt-get update && \
  apt-get install -y curl && \
  apt-get install -y  software-properties-common && \
  add-apt-repository ppa:webupd8team/java -y && \
  apt-get update && \
  echo oracle-java7-installer shared/accepted-oracle-license-v1-1 select true | /usr/bin/debconf-set-selections && \
  apt-get install -y oracle-java8-installer && \
  apt-get install -y oracle-java8-unlimited-jce-policy

RUN \
  curl -L -o sbt-$SBT_VERSION.deb http://dl.bintray.com/sbt/debian/sbt-$SBT_VERSION.deb && \
  dpkg -i sbt-$SBT_VERSION.deb && \
  rm sbt-$SBT_VERSION.deb && \
  apt-get install -y git && \
  apt-get install -y sbt && \
  sbt sbtVersion && \
  apt-get install -y openssh-server

# Install Scala
## Piping curl directly in tar
RUN \
  curl -fsL http://downloads.typesafe.com/scala/$SCALA_VERSION/scala-$SCALA_VERSION.tgz | tar xfz - -C /root/ && \
  echo >> /root/.bashrc && \
  echo 'export PATH=~/scala-$SCALA_VERSION/bin:$PATH' >> /root/.bashrc

RUN \
    DEBIAN_FRONTEND=noninteractive apt-get install -y krb5-user

RUN mkdir /var/run/sshd
RUN echo 'root:viktor' | chpasswd
RUN sed -i 's/PermitRootLogin prohibit-password/PermitRootLogin yes/' /etc/ssh/sshd_config

# SSH login fix. Otherwise user is kicked off after login
RUN sed 's@session\s*required\s*pam_loginuid.so@session optional pam_loginuid.so@g' -i /etc/pam.d/sshd

RUN adduser --disabled-password --gecos '' victorgarcia
RUN echo "victorgarcia:viktor" | chpasswd

ENV NOTVISIBLE "in users profile"
RUN echo "export VISIBLE=now" >> /etc/profile

RUN echo "force to clone repo from github 3"

WORKDIR "/home/victorgarcia"

RUN \
    git clone https://github.com/vgmartinez/kafka-akka-http.git

RUN chown -R victorgarcia:victorgarcia /home/victorgarcia

ENV JAVA_OPTS="-Djava.security.auth.login.config=/home/victorgarcia/kafka-akka-http/src/main/resources/kafka_jaas.conf -Djava.security.krb5.conf=/home/victorgarcia/kafka-akka-http/src/main/resources/krb5.conf -Djavax.security.auth.useSubjectCredsOnly=true"

EXPOSE 22
EXPOSE 9001

CMD ["/usr/sbin/sshd", "-D"]
CMD ["sbt", "run"]
