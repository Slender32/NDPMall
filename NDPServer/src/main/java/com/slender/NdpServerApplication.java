package com.slender;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
public class NdpServerApplication {
    void main(String[] args) {
        SpringApplication.run(NdpServerApplication.class, args);
    }
}
