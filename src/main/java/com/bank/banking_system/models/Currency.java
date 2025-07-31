package com.bank.banking_system.models;

public enum Currency {
    EUR,
    USD,
    GBP,
    ;

    public static Double getExchangeRate(Currency from, Currency to) {
        return switch (from) {
            case EUR -> switch (to) {
                case EUR -> 1.00000000D;
                case USD -> 1.14250470D;
                case GBP -> 0.86216864D;
            };
            case USD -> switch (to) {
                case EUR -> 0.87527000D;
                case USD -> 1.00000000D;
                case GBP -> 0.75485009D;
            };
            case GBP -> switch (to) {
                case EUR -> 1.16006000D;
                case USD -> 1.32477000D;
                case GBP -> 1.00000000D;
            };
        };
    }
}
