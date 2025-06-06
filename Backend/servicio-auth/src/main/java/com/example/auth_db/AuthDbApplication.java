package com.example.auth_db;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class AuthDbApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuthDbApplication.class, args);
    }

}
