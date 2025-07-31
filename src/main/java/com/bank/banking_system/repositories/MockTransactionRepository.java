package com.bank.banking_system.repositories;

import com.bank.banking_system.models.Transaction;
import org.antlr.v4.runtime.misc.Pair;
import org.springframework.stereotype.Repository;

import java.time.DateTimeException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@Repository("mockTransactionRepository")
public class MockTransactionRepository implements TransactionRepository {
    List<Transaction> transactions = new ArrayList<>();

    @Override
    public List<Transaction> getTransactions() {
        return transactions;
    }

    @Override
    public void setTransactions(List<Transaction> transactions) {
        if (transactions != null)
            this.transactions = transactions;
    }

    private Pair<Instant, Instant> getValidInterval(LocalDate dateFromInclusive, LocalDate dateToInclusive) {
        Instant filterFromInclusive = Instant.MIN;
        Instant filterToExclusive = Instant.MAX;

        try {
            filterFromInclusive = dateFromInclusive
                    .atStartOfDay(ZoneOffset.systemDefault())
                    .toInstant();
        } catch (NullPointerException | DateTimeException ignored) {}

        try {
            filterToExclusive = dateToInclusive
                    .atStartOfDay(ZoneOffset.systemDefault())
                    .plusDays(1L)
                    .toInstant();
        } catch (NullPointerException | DateTimeException ignored) {}

        return new Pair<>(filterFromInclusive, filterToExclusive);
    }

    @Override
    public Stream<Transaction> getTransactionsByDate(LocalDate dateFromInclusive, LocalDate dateToInclusive) {
        final Pair<Instant, Instant> filterInterval = getValidInterval(dateFromInclusive, dateToInclusive);

        return getTransactions()
                .stream()
                .filter(transaction ->
                        !transaction.time().toInstant().isBefore(filterInterval.a)
                                && transaction.time().toInstant().isBefore(filterInterval.b));
    }
}
