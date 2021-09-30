package scheadpp.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

import java.io.IOException;

@Configuration
@SpringBootApplication
@EnableEurekaClient
public class SchedCoreApplication {



    static final Logger log = LoggerFactory.getLogger(SchedCoreApplication.class);

    public static Logger getLogger() {
        return log;
    }


    public static void main(String[] args) throws IOException {
        SpringApplication.run(SchedCoreApplication.class, args);
    }

}
