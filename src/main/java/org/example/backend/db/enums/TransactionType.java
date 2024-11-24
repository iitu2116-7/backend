package org.example.backend.db.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TransactionType {
    DEPOSIT("Deposit", "Пополнение счета"),
    WITHDRAWAL("Withdrawal", "Вывод средств"),
    PURCHASE("Purchase", "Инвестиция"),
    SELL("Sell", "Продажа");

    private final String code;
    private final String description;


    public static TransactionType fromString(String code) {
        for (TransactionType transactionType : TransactionType.values()) {
            if (transactionType.getCode().equalsIgnoreCase(code)) {
                return transactionType;
            }
        }
        throw new IllegalArgumentException("Unknown transaction type: " + code);
    }
}
