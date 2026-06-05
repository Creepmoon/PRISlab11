package com.edusphere.materials;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.edusphere.materials", "com.edusphere.common"})
public class MaterialsServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(MaterialsServiceApplication.class, args);
    }
}
