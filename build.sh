call mvn clean package
docker build -t schedapp/core:latest -t schedapp/core:`git rev-list HEAD | wc -l | tr -d ' '` .
mkdir ./target/core
docker save -o ./target/core/image.tar schedapp/core
cp ./docker-compose.yml ./target/core/docker-compose.yml
docker-compose up
docker image prune -a