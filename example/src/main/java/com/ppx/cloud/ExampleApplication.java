package com.ppx.cloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.ppx.cloud.base.util.ApplicationUtils;

@SpringBootApplication
public class ExampleApplication {

    public static void main(String[] args) {
        
        ApplicationUtils.context = SpringApplication.run(ExampleApplication.class, args);
        
    }
    
}