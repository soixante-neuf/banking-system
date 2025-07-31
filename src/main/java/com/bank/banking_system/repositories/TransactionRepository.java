package com.bank.banking_system.repositories;

import com.bank.banking_system.models.Transaction;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Stream;

public interface TransactionRepository {
    List<Transaction> getTransactions();
    void setTransactions(List<Transaction> transactions);

    Stream<Transaction> getTransactionsByDate(LocalDate dateFromInclusive, LocalDate dateToInclusive);
}
