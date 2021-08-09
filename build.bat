call mvn clean package
docker build --tag core .
docker-compose up
docker image prune -a