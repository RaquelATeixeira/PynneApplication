package com.pyyne.challenge.bank.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AccountBalance {
    private String bank;
    private long accountId;
    private double amount;
    private String currency;
}