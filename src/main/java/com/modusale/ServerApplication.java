package com.modusale;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * sudo java -jar modusaleBuild/server.jar > log &
 * disown -a
 * ssh -i "starho_key.pem" ubuntu@ec2-3-35-173-138.ap-northeast-2.compute.amazonaws.com
 */

@ConfigurationPropertiesScan("com.modusale.utils.properties")
@SpringBootApplication
@EnableScheduling
public class ServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(ServerApplication.class, args);
    }
}
