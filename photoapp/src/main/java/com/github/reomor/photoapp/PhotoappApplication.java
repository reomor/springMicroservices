package com.github.reomor.photoapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@EnableEurekaServer
@SpringBootApplication
public class PhotoappApplication {

    public static void main(String[] args) {
        SpringApplication.run(PhotoappApplication.class, args);
    }

}
