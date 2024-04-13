package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
public class NshjShopApplication {

    public static void main(String[] args) {
        SpringApplication.run(NshjShopApplication.class, args);
    }

}
