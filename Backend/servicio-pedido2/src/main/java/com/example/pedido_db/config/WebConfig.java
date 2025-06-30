package com.example.pedido_db.config;


import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Configura Spring Boot para servir archivos estáticos desde la carpeta 'Frontend/public/'
        registry.addResourceHandler("/public/**")
                .addResourceLocations("file:Frontend/public/");
    }
}
