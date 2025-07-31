package com.bank.banking_system.services;

import com.bank.banking_system.models.Account;
import com.bank.banking_system.models.Currency;
import com.bank.banking_system.models.Transaction;
import org.junit.jupiter.api.Test;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class BasicTransactionServiceTest {
    @Test
    void balanceTest() {
        List<Transaction> sampleData = List.of(
                new Transaction(
                        new Account("GB55547671842817812165"),
                        new Timestamp(1715783515456L),
                        new Account("GB98808147610660312686"),
                        Optional.of("Subscription"),
                        970.47D,
                        Currency.GBP
                ),
                new Transaction(
                        new Account("GB84646537704259508821"),
                        new Timestamp(1716172551408L),
                        new Account("GB55547671842817812165"),
                        Optional.of("Groceries"),
                        738.83D,
                        Currency.EUR
                ),
                new Transaction(
                        new Account("GB69519391054363297071"),
                        new Timestamp(1715426999201L),
                        new Account("GB98808147610660312686"),
                        Optional.of("Online Purchase"),
                        47.45D,
                        Currency.USD
                )
        );
        final String correctAnswer = "-386.97 EUR";

        BasicTransactionService service = new BasicTransactionService();
        final String answer = service.calculateBalance("GB55547671842817812165", sampleData);

        assertEquals(correctAnswer, answer);
    }

}