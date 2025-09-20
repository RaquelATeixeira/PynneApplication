package com.pyyne.challenge.bank.domain;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Transaction {
    private String bank;
    private String type;
    private double amount;
    private String description;
}