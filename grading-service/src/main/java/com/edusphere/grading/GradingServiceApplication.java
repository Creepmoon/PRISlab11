package com.edusphere.grading;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.edusphere.grading", "com.edusphere.common"})
public class GradingServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(GradingServiceApplication.class, args);
    }
}
