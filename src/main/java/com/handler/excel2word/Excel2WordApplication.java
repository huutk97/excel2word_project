package com.handler.excel2word;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.handler.excel2word")
@EntityScan(basePackages = "com.handler.excel2word")
public class Excel2WordApplication {
    public static void main(String[] args) {
        SpringApplication.run(Excel2WordApplication.class, args);
    }
}
