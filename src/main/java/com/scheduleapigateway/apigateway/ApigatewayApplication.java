package com.scheduleapigateway.apigateway;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;


@Configuration
@SpringBootApplication
public class ApigatewayApplication {


    static final Logger log =
            LoggerFactory.getLogger(ApigatewayApplication.class);

    public static Logger getLogger() {
        return log;
    }



    public static void main(String[] args) {
        SpringApplication.run(ApigatewayApplication.class, args);
        System.out.println("инициализация завершена!");
    }



}
