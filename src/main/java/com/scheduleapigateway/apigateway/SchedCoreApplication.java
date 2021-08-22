package com.scheduleapigateway.apigateway;

import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import com.netflix.discovery.shared.Applications;
import com.netflix.eureka.EurekaServerContextHolder;
import com.netflix.eureka.registry.PeerAwareInstanceRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Configuration
@SpringBootApplication
@EnableEurekaClient
public class SchedCoreApplication {



    static final Logger log =
            LoggerFactory.getLogger(SchedCoreApplication.class);

        public static Logger getLogger() {
            return log;
        }


    public static void main(String[] args) throws IOException {
        SpringApplication.run(SchedCoreApplication.class, args);
    }

}
