package com.bank.banking_system.repositories;

import com.bank.banking_system.models.Account;
import com.bank.banking_system.models.Currency;
import com.bank.banking_system.models.Transaction;
import org.junit.jupiter.api.Test;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class MockTransactionRepositoryTest {
    @Test
    void mockRepositoryGetterAndSetterTests() {
        final MockTransactionRepository repository = new MockTransactionRepository();

        assertEquals(0, repository.getTransactions().size());

        final Transaction transaction = new Transaction(
                new Account("GB55547671842817812165"),
                new Timestamp(1715783515456L),
                new Account("GB98808147610660312686"),
                Optional.of("Subscription"),
                970.47D,
                Currency.GBP
        );
        List<Transaction> transactions = List.of(transaction, transaction, transaction, transaction, transaction);

        repository.setTransactions(transactions);
        assertEquals(transactions.size(), repository.getTransactions().size());
    }

    @Test
    void filterByDateTests() {
        final MockTransactionRepository repository = new MockTransactionRepository();

        final Transaction transaction1 = new Transaction(
                new Account("GB55547671842817812165"),
                new Timestamp(1715783515456L), // Wed May 15, 2024 14:31:55.456
                new Account("GB98808147610660312686"),
                Optional.of("Subscription"),
                970.47D,
                Currency.GBP
        );
        final Transaction transaction2 = new Transaction(
                new Account("GB55547671842817812165"),
                new Timestamp(1716172551408L), // Mon May 20, 2024 02:35:51.408
                new Account("GB98808147610660312686"),
                Optional.of("Subscription"),
                970.47D,
                Currency.GBP
        );

        repository.setTransactions(List.of(transaction1, transaction2));

        assertEquals(0, repository.getTransactionsByDate(LocalDate.MIN, LocalDate.of(2024, 5, 14)).toList().size());
        assertEquals(1, repository.getTransactionsByDate(LocalDate.MIN, LocalDate.of(2024, 5, 15)).toList().size());
        assertEquals(1, repository.getTransactionsByDate(LocalDate.MIN, LocalDate.of(2024, 5, 19)).toList().size());
        assertEquals(2, repository.getTransactionsByDate(LocalDate.MIN, LocalDate.of(2024, 5, 20)).toList().size());

        assertEquals(2, repository.getTransactionsByDate(LocalDate.of(2024, 5, 15), LocalDate.MAX).toList().size());
        assertEquals(1, repository.getTransactionsByDate(LocalDate.of(2024, 5, 16), LocalDate.MAX).toList().size());
        assertEquals(1, repository.getTransactionsByDate(LocalDate.of(2024, 5, 20), LocalDate.MAX).toList().size());
        assertEquals(0, repository.getTransactionsByDate(LocalDate.of(2024, 5, 21), LocalDate.MAX).toList().size());

        assertEquals(2, repository.getTransactionsByDate(LocalDate.of(2024, 5, 15), LocalDate.of(2024, 5, 20)).toList().size());
        assertEquals(1, repository.getTransactionsByDate(LocalDate.of(2024, 5, 16), LocalDate.of(2024, 5, 20)).toList().size());
        assertEquals(1, repository.getTransactionsByDate(LocalDate.of(2024, 5, 15), LocalDate.of(2024, 5, 19)).toList().size());
        assertEquals(0, repository.getTransactionsByDate(LocalDate.of(2024, 5, 16), LocalDate.of(2024, 5, 19)).toList().size());
    }
}