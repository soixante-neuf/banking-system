package com.bank.banking_system.models;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AccountTest {
    @Test
    void fromIbanTest() {
        final String sampleIban = "LT12345789";

        final Account acc1 = new Account(sampleIban);
        final Account acc2 = Account.fromIban(sampleIban);

        assertEquals(acc1, acc2);
        assertEquals(new Account(sampleIban), Account.fromIban(sampleIban));
    }
}