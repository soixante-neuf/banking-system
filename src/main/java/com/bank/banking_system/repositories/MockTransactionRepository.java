package com.bank.banking_system.repositories;

import com.bank.banking_system.models.Transaction;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@Repository
public class MockTransactionRepository implements TransactionRepository {
     List<Transaction> transactions = new ArrayList<>();

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        if (transactions != null)
            this.transactions = transactions;
    }

    public Stream<Transaction> getTransactionsByDate(LocalDate dateFromInclusive, LocalDate dateToInclusive) {
        dateFromInclusive = dateFromInclusive != null ? dateFromInclusive : LocalDate.MIN;
        dateToInclusive = dateToInclusive != null ? dateToInclusive : LocalDate.MAX;

        final Instant filterFromInclusive = dateFromInclusive.atStartOfDay(ZoneOffset.systemDefault()).toInstant();
        final Instant filterToExclusive = dateToInclusive.atStartOfDay(ZoneOffset.systemDefault()).plusDays(1L).toInstant();

        return getTransactions()
                .stream()
                .filter(transaction ->
                        !transaction.time().toInstant().isBefore(filterFromInclusive)
                                && transaction.time().toInstant().isBefore(filterToExclusive));
    }
}
