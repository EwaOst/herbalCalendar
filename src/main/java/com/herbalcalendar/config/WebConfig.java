package com.herbalcalendar.config;

import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // Zezwól na dostęp z wszystkich domen (można ograniczyć, np. tylko dla front-endu)
        registry.addMapping("/**").allowedOrigins("*"); // lub możesz podać np. allowedOrigins("http://localhost:4200")
    }
}
