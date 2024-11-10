package org.example.backend.db.entites;

import jakarta.persistence.*;
import lombok.*;
import org.example.backend.db.BaseEntity;

import java.math.BigDecimal;

@Entity
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "accounts")
public class Account extends BaseEntity {

    @JoinColumn(name = "customer")
    @ManyToOne
    private Customer customer;

    @Column(name = "balance")
    private BigDecimal balanceInKZT;
}
