package org.example.backend.utils;

import org.example.backend.db.entites.Transaction;
import org.example.backend.dto.dtos.TransactionDTO;
import org.springframework.stereotype.Component;

@Component
public class TransactionMapper {

    public TransactionDTO toDto(Transaction transaction) {
        return new TransactionDTO (
                transaction.getAmountInKZT(),
                String.valueOf(transaction.getTransactionType())
        );
    }
}
