package com.gitee.sop.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.gitee.sop")
public class SopGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(SopGatewayApplication.class, args);
    }

}

