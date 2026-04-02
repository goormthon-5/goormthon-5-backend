package com.goormthon5backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class Goormthon5BackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(Goormthon5BackendApplication.class, args);
    }
}
