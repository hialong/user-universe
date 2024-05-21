package com.apiAssign.apiassign;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan({"com.decade.apiassignclientsdk","com.apiAssign.apiassign"})
public class ApiAssignApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApiAssignApplication.class, args);
    }

}
