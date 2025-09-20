package com.pyyne.challenge.bank.adapters.bank1;

import com.bank1.integration.Bank1AccountSource;
import com.bank1.integration.Bank1Transaction;
import com.pyyne.challenge.bank.domain.AccountBalance;
import com.pyyne.challenge.bank.domain.Transaction;
import com.pyyne.challenge.bank.ports.BankClient;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public class Bank1Adapter implements BankClient {

    private final Bank1AccountSource accountSource = new Bank1AccountSource();

    @Override
    public String bankId() {
        return "bank1";
    }

    @Override
    public List<AccountBalance> getBalances(long accountId) {
        double balance = accountSource.getAccountBalance(accountId);
        return List.of(new AccountBalance(bankId(), accountId, balance, "UNKNOWN"));
    }

    @Override
    public List<Transaction> getTransactions(long accountId, Date fromDate, Date toDate) {
        List<Bank1Transaction> txs = accountSource.getTransactions(accountId, fromDate, toDate);
        return txs.stream()
                .map(t -> new Transaction(
                        bankId(),
                        t.getType() == Bank1Transaction.TYPE_CREDIT ? "CREDIT" : "DEBIT",
                        t.getAmount(),
                        t.getText()
                ))
                .toList();
    }
}
