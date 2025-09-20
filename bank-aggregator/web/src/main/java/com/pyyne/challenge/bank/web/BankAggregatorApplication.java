package com.pyyne.challenge.bank.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.pyyne.challenge.bank")
public class BankAggregatorApplication {
    public static void main(String[] args) {
        SpringApplication.run(BankAggregatorApplication.class, args);
    }
}