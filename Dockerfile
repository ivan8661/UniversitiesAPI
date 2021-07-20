FROM openjdk:16
MAINTAINER Ivan Poltorakov <ivan@poltorakov.ru>
ADD ./target/core.jar core.jar
ENTRYPOINT ["java","-jar","core.jar"]
EXPOSE 8090