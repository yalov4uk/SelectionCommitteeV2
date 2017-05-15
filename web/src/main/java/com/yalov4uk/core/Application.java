package com.yalov4uk.core;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;

/**
 * Created by valera on 5/7/17.
 */
@SpringBootApplication
@ComponentScan(basePackages = {"com.yalov4uk"})
@EntityScan(basePackages = "com.yalov4uk")
public class Application {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(Application.class, args);
    }
}
