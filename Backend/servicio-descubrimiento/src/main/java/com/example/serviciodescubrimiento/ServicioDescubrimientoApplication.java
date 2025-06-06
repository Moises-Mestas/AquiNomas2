package com.example.serviciodescubrimiento;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class ServicioDescubrimientoApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServicioDescubrimientoApplication.class, args);
    }

}
