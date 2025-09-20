package com.pyyne.challenge.bank.ports;

import com.pyyne.challenge.bank.domain.AccountBalance;
import com.pyyne.challenge.bank.domain.Transaction;

import java.util.Date;
import java.util.List;

public interface BankClient {
    String bankId();
    List<AccountBalance> getBalances(long accountId);
    List<Transaction> getTransactions(long accountId, Date fromDate, Date toDate);
}
