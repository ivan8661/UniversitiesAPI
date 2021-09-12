#!/bin/bash
mvn clean package

buildBranch=$(git rev-parse --abbrev-ref HEAD | tr -d '-'| tr -d '/')
buildNumber=$(git rev-list HEAD | wc -l | tr -d ' ')
buildVersion="$buildBranch.$buildNumber"

exportDirectory="target/docker/core"

echo Y | docker image prune -a
docker build -t schedapp/core:latest -t schedapp/core:$buildVersion .
mkdir -p ./$exportDirectory
echo Y | docker image prune
docker save -o ./$exportDirectory/image.tar schedapp/core
cp ./docker-compose.yml ./$exportDirectory/docker-compose.yml