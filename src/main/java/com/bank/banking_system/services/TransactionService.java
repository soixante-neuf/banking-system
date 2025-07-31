package com.bank.banking_system.services;

import com.bank.banking_system.models.Transaction;
import com.opencsv.exceptions.CsvException;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.List;

public interface TransactionService {
    List<Transaction> importTransactions(Reader importReader) throws IOException, CsvException;
    void exportTransactions(Writer exportWriter, List<Transaction> transactions);
    String calculateBalance(String accountIban, List<Transaction> transactions);
}
