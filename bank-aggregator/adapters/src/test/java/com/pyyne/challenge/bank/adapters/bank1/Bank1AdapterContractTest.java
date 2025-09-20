package com.pyyne.challenge.bank.adapters.bank1;

import com.pyyne.challenge.bank.adapters.contract.BankClientContractTest;
import com.pyyne.challenge.bank.ports.BankClient;

public class Bank1AdapterContractTest extends BankClientContractTest {
    @Override
    protected BankClient createClient() {
        return new Bank1Adapter();
    }
}
