# kafka-akka-http

## Build 

package sbt

    sbt "set test in assembly := {}" clean assembly

docker build    

    sudo docker build -t dev.docker.kickstartteam.es:5000/kst/cognosfera/demo-kafka-api:0.0.1 .