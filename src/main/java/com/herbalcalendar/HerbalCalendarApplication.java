package com.herbalcalendar;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan(basePackages = {"com.herbalcalendar.model"})
public class HerbalCalendarApplication {
            public static void main(String[] args) {
                SpringApplication.run(HerbalCalendarApplication.class, args);
            }
        }