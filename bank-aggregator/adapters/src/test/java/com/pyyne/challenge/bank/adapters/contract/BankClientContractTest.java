package com.pyyne.challenge.bank.adapters.contract;

import com.pyyne.challenge.bank.domain.AccountBalance;
import com.pyyne.challenge.bank.domain.Transaction;
import com.pyyne.challenge.bank.ports.BankClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public abstract class BankClientContractTest {

    protected BankClient client;
    protected long accountId;

    protected abstract BankClient createClient();

    @BeforeEach
    void setUp() {
        this.client = createClient();
        this.accountId = 123L;
    }

    @Test
    void balances_shouldReturnUnifiedDomainModel() {
        List<AccountBalance> balances = client.getBalances(accountId);
        assertThat(balances).isNotNull();

        for (AccountBalance b : balances) {
            assertThat(b.getBank()).isEqualTo(client.bankId());
            assertThat(b.getAccountId()).isEqualTo(accountId);
            assertThat(b.getCurrency()).isNotNull();
            assertThat(b.getAmount()).isNotNaN();
        }
    }

    @Test
    void transactions_shouldReturnUnifiedDomainModel() {
        Date from = new Date(0L);
        Date to = new Date();

        List<Transaction> txs = client.getTransactions(accountId, from, to);
        assertThat(txs).isNotNull();

        for (Transaction t : txs) {
            assertThat(t.getBank()).isEqualTo(client.bankId());
            assertThat(t.getType()).isIn("CREDIT", "DEBIT");
            assertThat(t.getDescription()).isNotNull();
            assertThat(t.getAmount()).isNotNaN();
        }
    }
}

