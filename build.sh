#!/bin/bash
mvn clean package

buildBranch=$(git rev-parse --abbrev-ref HEAD)
buildNumber=$(git rev-list HEAD | wc -l | tr -d ' ')
buildVersion="$buildBranch.$buildNumber"

echo Y | docker image prune -a
docker build -t schedapp/core:latest -t schedapp/core:$buildVersion .
mkdir ./target/core
echo Y | docker image prune
docker save -o ./target/core/image.tar schedapp/core
cp ./docker-compose.yml ./target/core/docker-compose.yml