package com.banco.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class BancoApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(BancoApiApplication.class, args);
    }

}
