package com.pyyne.challenge.bank.application;

import com.pyyne.challenge.bank.domain.AccountBalance;
import com.pyyne.challenge.bank.domain.Transaction;
import com.pyyne.challenge.bank.ports.BankClient;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class AggregationService {

    private final List<BankClient> clients;

    public AggregationService(List<BankClient> clients) {
        this.clients = clients;
    }

    public List<AccountBalance> aggregateBalances(long accountId) {
        return clients.stream()
                .flatMap(c -> c.getBalances(accountId).stream())
                .toList();
    }

    public List<Transaction> aggregateTransactions(long accountId, Date fromDate, Date toDate) {
        return clients.stream()
                .flatMap(c -> c.getTransactions(accountId, fromDate, toDate).stream())
                .toList();
    }
}
