From openjdk:16
copy ./target/apigateway-0.0.1-ScheduleAPIGateway.jar apigateway-0.0.1-ScheduleAPIGateway.jar
CMD ["java","-jar","apigateway-0.0.1-ScheduleAPIGateway.jar"]