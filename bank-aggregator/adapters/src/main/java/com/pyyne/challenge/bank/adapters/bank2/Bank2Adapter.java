package com.pyyne.challenge.bank.adapters.bank2;

import com.bank2.integration.Bank2AccountBalance;
import com.bank2.integration.Bank2AccountSource;
import com.bank2.integration.Bank2AccountTransaction;
import com.pyyne.challenge.bank.domain.AccountBalance;
import com.pyyne.challenge.bank.domain.Transaction;
import com.pyyne.challenge.bank.ports.BankClient;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public class Bank2Adapter implements BankClient {

    private final Bank2AccountSource accountSource = new Bank2AccountSource();

    @Override
    public String bankId() {
        return "bank2";
    }

    @Override
    public List<AccountBalance> getBalances(long accountId) {
        Bank2AccountBalance b = accountSource.getBalance(accountId);
        return List.of(new AccountBalance(bankId(), accountId, b.getBalance(), b.getCurrency()));
    }

    @Override
    public List<Transaction> getTransactions(long accountId, Date fromDate, Date toDate) {
        List<Bank2AccountTransaction> txs = accountSource.getTransactions(accountId, fromDate, toDate);
        return txs.stream()
                .map(t -> new Transaction(
                        bankId(),
                        t.getType() == Bank2AccountTransaction.TRANSACTION_TYPES.CREDIT ? "CREDIT" : "DEBIT",
                        t.getAmount(),
                        t.getText()
                ))
                .toList();
    }
}