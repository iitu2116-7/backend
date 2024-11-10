package org.example.backend.db.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Currency {
    USD("USD", "$"),
    EUR("EUR", "€"),
    RUB("RUB", "₽"),
    KGS("KGS", "лв"),
    GBP("GBP", "£"),
    CNY("CNY", "¥"),
    KZT("KZT", "₸");;

    private final String code;
    private final String symbol;

    public static Currency fromString(String code) {
        for (Currency currency : Currency.values()) {
            if (currency.getCode().equalsIgnoreCase(code)) {
                return currency;
            }
        }
        throw new IllegalArgumentException("Unknown currency: " + code);
    }
}
