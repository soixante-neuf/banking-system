package com.bank.banking_system.models;

import java.sql.Timestamp;
import java.util.Optional;

public record Transaction(
        Account sender,
        Timestamp time,
        Account beneficiary,
        Optional<String> comment,
        Double amount,
        Currency currency
) {}
