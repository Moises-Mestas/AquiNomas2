package com.example.servicioventa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class ServicioVentaApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServicioVentaApplication.class, args);
    }

}
