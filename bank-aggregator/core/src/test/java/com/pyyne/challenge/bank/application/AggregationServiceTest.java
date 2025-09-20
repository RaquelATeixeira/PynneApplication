package com.pyyne.challenge.bank.application;

import com.pyyne.challenge.bank.domain.AccountBalance;
import com.pyyne.challenge.bank.domain.Transaction;
import com.pyyne.challenge.bank.ports.BankClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AggregationServiceTest {

    @Mock BankClient bank1;
    @Mock BankClient bank2;

    AggregationService service;

    @BeforeEach
    void setUp() {
        service = new AggregationService(List.of(bank1, bank2));
    }

    @Test
    void aggregatesBalances() {
        when(bank1.getBalances(123L))
                .thenReturn(List.of(new AccountBalance("bank1", 123L, 100.0, "UNKNOWN")));
        when(bank2.getBalances(123L))
                .thenReturn(List.of(new AccountBalance("bank2", 123L, 50.0, "USD")));

        List<AccountBalance> result =
        service.aggregateBalances(123L);

        assertThat(result).hasSize(2);
        assertThat(result).extracting(AccountBalance::getBank)
                          .containsExactlyInAnyOrder("bank1", "bank2");
        assertThat(result).extracting(AccountBalance::getAmount)
                          .containsExactlyInAnyOrder(100.0, 50.0);
    }

    @Test
    void aggregatesTransactions() {
        Date from = new Date(0L);
        Date to = new Date();

        when(bank1.getTransactions(123L, from, to))
                .thenReturn(List.of(new Transaction("bank1", "DEBIT", 10.0, "Coffee")));
        when(bank2.getTransactions(123L, from, to))
                .thenReturn(List.of(new Transaction("bank2", "CREDIT", 25.0, "Salary")));

        List<Transaction> result = service.aggregateTransactions(123L, from, to);

        assertThat(result).hasSize(2);
        assertThat(result).extracting(Transaction::getType)
                          .containsExactlyInAnyOrder("DEBIT", "CREDIT");
        assertThat(result).extracting(Transaction::getDescription)
                          .contains("Coffee", "Salary");
    }
}
