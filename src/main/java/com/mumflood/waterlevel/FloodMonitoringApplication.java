package com.mumflood.waterlevel;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class FloodMonitoringApplication {
    public static void main(String[] args) {
        SpringApplication.run(FloodMonitoringApplication.class, args);
    }
}
