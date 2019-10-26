package com.github.iauglov.mariya.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class MariyaApplication {

    public static void main(String[] args) {
        SpringApplication.run(MariyaApplication.class, args);
    }

}
