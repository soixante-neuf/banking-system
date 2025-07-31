package com.bank.banking_system.models;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CurrencyTest {
    @Test
    void exchangeRateOfCurrencyToItselfIsOne() {
        assertEquals(1.0, Currency.getExchangeRate(Currency.EUR, Currency.EUR));
        assertEquals(1.0, Currency.getExchangeRate(Currency.USD, Currency.USD));
        assertEquals(1.0, Currency.getExchangeRate(Currency.GBP, Currency.GBP));
    }

    @Test
    void exchangeRateParameterFlipTests() {
        final double maxError = 0.000001D;
        assertTrue(maxError > 1.0D - Currency.getExchangeRate(Currency.EUR, Currency.USD) * Currency.getExchangeRate(Currency.USD, Currency.EUR));
        assertTrue(maxError > 1.0D - Currency.getExchangeRate(Currency.EUR, Currency.GBP) * Currency.getExchangeRate(Currency.GBP, Currency.EUR));
        assertTrue(maxError > 1.0D - Currency.getExchangeRate(Currency.USD, Currency.GBP) * Currency.getExchangeRate(Currency.GBP, Currency.USD));
    }
}