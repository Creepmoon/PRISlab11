package com.edusphere.adaptive;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.edusphere.adaptive", "com.edusphere.common"})
public class AdaptiveLearningApplication {
    public static void main(String[] args) {
        SpringApplication.run(AdaptiveLearningApplication.class, args);
    }
}
