package com.pyyne.challenge.bank.web;

import com.pyyne.challenge.bank.application.AggregationService;
import com.pyyne.challenge.bank.domain.AccountBalance;
import com.pyyne.challenge.bank.domain.Transaction;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.Date;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BankController.class)
class BankControllerTest {

    @Autowired MockMvc mvc;
    @MockBean AggregationService service;

    @Test
    void balances_returnsAggregatedJson() throws Exception {
        when(service.aggregateBalances(123L))
                .thenReturn(List.of(
                        new AccountBalance("bank1", 123L, 100.0, "UNKNOWN"),
                        new AccountBalance("bank2", 123L, 50.5, "USD")
                ));

        mvc.perform(get("/api/customers/123/balances"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].bank").value("bank1"))
                .andExpect(jsonPath("$[0].accountId").value(123))
                .andExpect(jsonPath("$[0].amount").value(100.0))
                .andExpect(jsonPath("$[0].currency").value("UNKNOWN"))
                .andExpect(jsonPath("$[1].bank").value("bank2"))
                .andExpect(jsonPath("$[1].currency").value("USD"));
    }

    @Test
    void transactions_acceptsFromToAndReturnsAggregatedJson() throws Exception {
        Instant from = OffsetDateTime.parse("2025-01-01T00:00:00Z").toInstant();
        Instant to   = OffsetDateTime.parse("2025-02-01T00:00:00Z").toInstant();

        when(service.aggregateTransactions(123L, Date.from(from), Date.from(to)))
                .thenReturn(List.of(
                        new Transaction("bank1", "DEBIT", 10.0, "Coffee"),
                        new Transaction("bank2", "CREDIT", 25.0, "Salary")
                ));

        mvc.perform(get("/api/customers/123/transactions")
                        .param("from", "2025-01-01T00:00:00Z")
                        .param("to", "2025-02-01T00:00:00Z"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].bank").value("bank1"))
                .andExpect(jsonPath("$[0].type").value("DEBIT"))
                .andExpect(jsonPath("$[0].amount").value(10.0))
                .andExpect(jsonPath("$[0].description").value("Coffee"))
                .andExpect(jsonPath("$[1].bank").value("bank2"))
                .andExpect(jsonPath("$[1].type").value("CREDIT"));
    }

    @Test
    void transactions_defaultsFromEpochAndToNowWhenNotProvided() throws Exception {
        when(service.aggregateTransactions(
                eq(123L),
                eq(new Date(0L)),
                any(Date.class)
        )).thenReturn(List.of(
                new Transaction("bank1", "DEBIT", 9.0, "Snack")
        ));

        mvc.perform(get("/api/customers/123/transactions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].description").value("Snack"));
    }
}
