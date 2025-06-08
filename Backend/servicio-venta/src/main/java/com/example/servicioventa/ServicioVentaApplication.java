package com.example.servicioventa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients(basePackages = "com.example.servicioventa.feing")
@SpringBootApplication
public class ServicioVentaApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServicioVentaApplication.class, args);
    }

}
