#
# Scala, sbt, krb5
#

# Pull base image
FROM ubuntu:16.04

ENV SCALA_VERSION 2.11.8
ENV SBT_VERSION 0.13.13

# Install java
RUN \
  apt-get update && \
  apt-get install -y curl && \
  apt-get install -y  software-properties-common && \
  add-apt-repository ppa:webupd8team/java -y && \
  apt-get update && \
  echo oracle-java7-installer shared/accepted-oracle-license-v1-1 select true | /usr/bin/debconf-set-selections && \
  apt-get install -y oracle-java8-installer && \
  apt-get install -y oracle-java8-unlimited-jce-policy

# Install sbt, git and openssh
RUN \
  curl -L -o sbt-$SBT_VERSION.deb http://dl.bintray.com/sbt/debian/sbt-$SBT_VERSION.deb && \
  dpkg -i sbt-$SBT_VERSION.deb && \
  rm sbt-$SBT_VERSION.deb && \
  apt-get install -y git && \
  apt-get install -y sbt && \
  sbt sbtVersion && \
  apt-get install -y openssh-server

# Install Scala
RUN \
  curl -fsL http://downloads.typesafe.com/scala/$SCALA_VERSION/scala-$SCALA_VERSION.tgz | tar xfz - -C /root/ && \
  echo >> /root/.bashrc && \
  echo 'export PATH=~/scala-$SCALA_VERSION/bin:$PATH' >> /root/.bashrc

# Install kerberos client
RUN \
    DEBIAN_FRONTEND=noninteractive apt-get install -y krb5-user

RUN mkdir /var/run/sshd
RUN echo 'root:root' | chpasswd
RUN sed -i 's/PermitRootLogin prohibit-password/PermitRootLogin yes/' /etc/ssh/sshd_config

# SSH login fix. Otherwise user is kicked off after login
RUN \
    sed 's@session\s*required\s*pam_loginuid.so@session optional pam_loginuid.so@g' -i /etc/pam.d/sshd && \
    adduser --disabled-password --gecos '' kst && echo "kst:kst" | chpasswd

ENV NOTVISIBLE "in users profile"
RUN echo "export VISIBLE=now" >> /etc/profile && echo "force to clone repo from github 6"

WORKDIR "/home/kst"

RUN git clone https://github.com/vgmartinez/kafka-akka-http.git

ENV JAVA_OPTS="-Djava.security.auth.login.config=/home/kst/kafka-akka-http/src/main/resources/kafka_jaas.conf -Djavax.security.auth.useSubjectCredsOnly=false"

WORKDIR kafka-akka-http

RUN \
    ln -sf /home/kst/kafka-akka-http/src/main/resources/krb5.conf /etc/krb5.conf && \
    chown -R kst:kst /home/kst

EXPOSE 22
EXPOSE 9002

#CMD ["sbt", "run"]
CMD ["/usr/sbin/sshd", "-D"]