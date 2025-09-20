package com.pyyne.challenge.bank.web;

import com.pyyne.challenge.bank.application.AggregationService;
import com.pyyne.challenge.bank.domain.AccountBalance;
import com.pyyne.challenge.bank.domain.Transaction;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/customers/{id}")
public class BankController {

    private final AggregationService service;

    public BankController(AggregationService service) {
        this.service = service;
    }

    @GetMapping("/balances")
    public List<AccountBalance> balances(@PathVariable("id") long id) {
        return service.aggregateBalances(id);
    }

    @GetMapping("/transactions")
    public List<Transaction> transactions(
            @PathVariable("id") long id,
            @RequestParam(name = "from", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Date from,
            @RequestParam(name = "to", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Date to
    ) {
        Date fromDate = (from == null) ? new Date(0L) : from;
        Date toDate   = (to == null)   ? new Date()   : to;
        return service.aggregateTransactions(id, fromDate, toDate);
    }
}
