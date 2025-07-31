package com.bank.banking_system.services;

import com.bank.banking_system.models.Account;
import com.bank.banking_system.models.Currency;
import com.bank.banking_system.models.Transaction;
import com.opencsv.*;
import com.opencsv.exceptions.CsvException;
import org.springframework.stereotype.Service;

import java.io.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class BasicTransactionService implements TransactionService {
    private static final String[] csvHeader = new String[] {
            "Account Number",
            "Date/Time",
            "Beneficiary",
            "Comment",
            "Amount",
            "Currency",
    };
    private static final CSVParser parser = new CSVParserBuilder()
            .withIgnoreQuotations(false)
            .withSeparator(',')
            .build();

    private Optional<Transaction> readTransaction(String[] csvRow) {
        final int transactionFieldCount = 6;
        if (csvRow == null || Arrays.stream(csvRow).toList().size() != transactionFieldCount)
            return Optional.empty();

        try {
            Account             sender      = Account.fromIban(csvRow[0]);
            Timestamp           time        = Timestamp.valueOf(csvRow[1]);
            Account             beneficiary = Account.fromIban(csvRow[2]);
            Optional<String>    comment     = csvRow[3].equals("-") ? Optional.empty() : Optional.of(csvRow[3]);
            Double              amount      = Double.valueOf(csvRow[4]);
            Currency            currency    = Currency.valueOf(csvRow[5]);

            final Transaction parsedTransaction = new Transaction(
                    sender,
                    time,
                    beneficiary,
                    comment,
                    amount,
                    currency);

            return Optional.of(parsedTransaction);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public List<Transaction> importTransactions(Reader importReader) throws IOException, CsvException {
        if (importReader == null)
            throw new IOException("Null reader for CSV import was passed.");

        try (CSVReader csvReader = new CSVReaderBuilder(importReader)
                .withSkipLines(1)
                .withCSVParser(parser)
                .build()) {

            List<String[]> data = csvReader.readAll();
            List<Transaction> transactions = new ArrayList<>();

            for (String[] row : data) {
                final var parsedTransaction = readTransaction(row);
                parsedTransaction.ifPresent(transactions::add);
            }

            return transactions;
        }
    }

    private String[] transactionToCsvRow(Transaction transaction) {
        return new String[] {
                transaction.sender().iban(),
                transaction.time().toString(),
                transaction.beneficiary().iban(),
                transaction.comment().orElse("-"),
                transaction.amount().toString(),
                transaction.currency().toString(),
        };
    }

    public void exportTransactions(Writer exportWriter, List<Transaction> transactions) {
        final CSVParserWriter writer = new CSVParserWriter(exportWriter, parser, "\n");
        final List<String[]> rows = transactions
                .stream()
                .map(this::transactionToCsvRow)
                .toList();

        writer.writeNext(csvHeader);
        writer.writeAll(rows);
    }


    public String calculateBalance(String accountIban, List<Transaction> transactions) {
        boolean accountHasTransactions = false;
        double balance = 0.0D;
        Currency currentCurrency = Currency.EUR;

        for (Transaction transaction : transactions) {
            final boolean isSender = transaction.sender().iban().equals(accountIban);
            final boolean isBeneficiary = transaction.beneficiary().iban().equals(accountIban);

            accountHasTransactions |= isSender;
            accountHasTransactions |= isBeneficiary;

            final double exchangeRate = Currency.getExchangeRate(currentCurrency, transaction.currency());
            if (isSender && !isBeneficiary) {
                balance = balance * exchangeRate - transaction.amount();
                currentCurrency = transaction.currency();
            } else if (!isSender && isBeneficiary) {
                balance = balance * exchangeRate + transaction.amount();
                currentCurrency = transaction.currency();
            }
        }

        balance = Math.round(balance * 100.0) / 100.0;

        return accountHasTransactions
                ? String.format("%.2f %s", balance, currentCurrency.toString())
                : "Account does not have any transactions.";
    }
}
