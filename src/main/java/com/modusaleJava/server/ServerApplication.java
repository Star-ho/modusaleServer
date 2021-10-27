package com.modusaleJava.server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * sudo java -jar modusaleBuild/server.jar > log &
 * disown -a
 * ssh -i "starho_key.pem" ubuntu@ec2-3-35-173-138.ap-northeast-2.compute.amazonaws.com
 */

@SpringBootApplication
@EnableScheduling
public class ServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(ServerApplication.class, args);
    }

    private ModusaleBatch modusaleBatch;

    @Autowired
    public void setModusaleBatch(ModusaleBatch modusaleBatch) {
        this.modusaleBatch = modusaleBatch;
    }

    @Bean
    public CommandLineRunner dataLoader(){
        return new CommandLineRunner() {
            @Override
            public void run(String... args) {
                modusaleBatch.consume();
            }
        };
    }
}
