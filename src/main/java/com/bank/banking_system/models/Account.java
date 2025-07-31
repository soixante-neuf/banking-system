package com.bank.banking_system.models;

public record Account(
        String iban
) {
    public static Account fromIban(String iban) {
        return new Account(iban);
    }
}
