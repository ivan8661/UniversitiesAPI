call mvn clean package
docker build -t schedapp/core:latest -t schedapp/core:`git rev-list HEAD | wc -l | tr -d ' '` .
docker-compose up
docker image prune -a