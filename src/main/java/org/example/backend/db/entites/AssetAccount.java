package org.example.backend.db.entites;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.backend.db.BaseEntity;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "asset_accounts")
public class AssetAccount extends BaseEntity {

    @JoinColumn(name = "customer")
    @ManyToOne
    private Customer customer;

    @JoinColumn(name = "crypto")
    @ManyToOne
    private CryptoPrices crypto;

    @Column(name = "quantity")
    private BigDecimal quantity;

    @Column(name = "purchased_amount")
    private BigDecimal purchasedAmount;

    @Column(name = "profit")
    private BigDecimal profit;
}
