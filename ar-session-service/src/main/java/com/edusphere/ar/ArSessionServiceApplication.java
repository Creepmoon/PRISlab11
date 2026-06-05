package com.edusphere.ar;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.edusphere.ar", "com.edusphere.common"})
public class ArSessionServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(ArSessionServiceApplication.class, args);
    }
}
