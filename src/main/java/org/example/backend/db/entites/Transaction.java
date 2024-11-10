package org.example.backend.db.entites;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.backend.db.BaseEntity;
import org.example.backend.db.enums.TransactionType;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "transactions")
public class Transaction extends BaseEntity {

    @JoinColumn(name = "customer")
    @ManyToOne
    private Customer customer;

    @JoinColumn(name = "account")
    @ManyToOne
    private Account account;

    @Column(name = "amount")
    private BigDecimal amountInKZT;

    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;


}
