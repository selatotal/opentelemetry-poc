#!/bin/bash
gradle createDockerfile
docker build -t ${1} .
minikube image load ${1}