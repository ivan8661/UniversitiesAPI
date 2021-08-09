FROM adoptopenjdk/openjdk16:x86_64-alpine-jre16u-nightly
MAINTAINER Ivan Poltorakov <ivan@poltorakov.ru>
ADD ./target/core.jar core.jar
ENTRYPOINT java -jar core.jar core
EXPOSE 8080