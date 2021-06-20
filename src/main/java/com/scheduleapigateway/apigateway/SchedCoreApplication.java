package com.scheduleapigateway.apigateway;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;


@Configuration
@SpringBootApplication
public class SchedCoreApplication {


    static final Logger log =
            LoggerFactory.getLogger(SchedCoreApplication.class);

    public static Logger getLogger() {
        return log;
    }



    public static void main(String[] args) {
        SpringApplication.run(SchedCoreApplication.class, args);
        System.out.println("инициализация завершена!");
    }



}
