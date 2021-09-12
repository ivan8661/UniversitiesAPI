#!/bin/bash

moduleName="core"

exportDirectory="target/docker/$moduleName"
buildBranch=$(git rev-parse --abbrev-ref HEAD | tr -d '-'| tr -d '/')
buildNumber=$(git rev-list HEAD | wc -l | tr -d ' ')
buildVersion="$buildBranch.$buildNumber"

mvn -Dmaven.test.skip=true clean package
echo Y | docker image prune -a
docker build -t schedapp/$moduleName:latest -t schedapp/$moduleName:$buildVersion .
mkdir -p ./$exportDirectory
echo Y | docker image prune
docker save -o ./$exportDirectory/image.tar schedapp/$moduleName
cp ./docker-compose.yml ./$exportDirectory/docker-compose.yml