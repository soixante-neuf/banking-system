package com.bank.banking_system.controllers;

import com.bank.banking_system.models.BalanceRequest;
import com.bank.banking_system.models.Transaction;
import com.bank.banking_system.repositories.TransactionRepository;
import com.bank.banking_system.services.TransactionService;
import com.opencsv.exceptions.CsvException;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.time.LocalDate;
import java.util.*;


@RestController
@RequestMapping("/transactions")
public class TransactionController {
    private final TransactionRepository transactionRepository;
    private final TransactionService transactionService;

    public TransactionController(TransactionRepository transactionRepository, TransactionService transactionService) {
        this.transactionRepository = transactionRepository;
        this.transactionService = transactionService;
    }

    @PostMapping(value = "/import", consumes = {"multipart/form-data"})
    public ResponseEntity<String> importTransactions(@RequestPart("file")MultipartFile importFile) {
        try {
            final Reader reader = new BufferedReader(new InputStreamReader(importFile.getInputStream()));
            final List<Transaction> parsedTransactions = transactionService.importTransactions(reader);

            transactionRepository.setTransactions(parsedTransactions);

            return ResponseEntity.ok()
                    .body(String.format("Imported %d transaction(s).", parsedTransactions.size()));
        } catch (IOException | CsvException e) {
            return ResponseEntity.badRequest()
                    .body(e.getMessage());
        }
    }

    @GetMapping("/export")
    public ResponseEntity<String> exportTransactions(
            HttpServletResponse response,
            @RequestParam(value = "dateFrom", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateFrom,
            @RequestParam(value = "dateTo", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateTo
    ) {
        if (response == null)
            return ResponseEntity.badRequest().build();

        response.setContentType("text/csv; charset=UTF-8");
        response.setHeader("Content-Disposition", "attachment; filename=\"transactions.csv\"");

        final List<Transaction> relevantTransactions = transactionRepository
                .getTransactionsByDate(dateFrom, dateTo)
                .toList();

        try {
            transactionService.exportTransactions(response.getWriter(), relevantTransactions);
        } catch (IOException e) {
            return ResponseEntity.internalServerError()
                    .body(e.getMessage());
        }

        return ResponseEntity.ok().build();
    }

    @PostMapping(value = "/balance")
    public ResponseEntity<String> calculateAccountBalance(
            @RequestBody BalanceRequest requestBody,
            @RequestParam(value = "dateFrom", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateFrom,
            @RequestParam(value = "dateTo", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateTo
    ) {
        if (requestBody == null)
            return ResponseEntity.badRequest().build();

        if (requestBody.accountIban() == null)
            return ResponseEntity.badRequest()
                    .body("Account (IBAN) number was not specified");

        final List<Transaction> relevantTransactions = transactionRepository
                .getTransactionsByDate(dateFrom, dateTo)
                .toList();

        return ResponseEntity.ok()
                .body(transactionService.calculateBalance(requestBody.accountIban(), relevantTransactions));
    }
}
