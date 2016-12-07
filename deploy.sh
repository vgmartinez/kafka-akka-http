#!/usr/bin/env bash

sbt assembly

sudo docker build -t docker.qa-genesis.es:5000/kst/cognosfera/demo-kafka-api:0.0.1 .

sudo docker push docker.qa-genesis.es:5000/kst/cognosfera/demo-kafka-api:0.0.1

sudo docker run -it --rm --env-file ~/.genesis/cognosfera.list --env-file ~/envvars.list dev.docker.kickstartteam.es:5000/kst/architecture/ops:0.7.3 deploy_app -r KafkaRESTAPI -t 0.0.1