package org.example.backend.db.entites;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.backend.db.BaseEntity;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "crypto_prices")
public class CryptoPrices extends BaseEntity {

    @Column(name = "symbol")
    private String symbol;

    @Column(name = "price")
    private double price;

    @Column(name = "open_price")
    private double openPrice;

    @Column(name = "high_price")
    private double highPrice;

    @Column(name = "low_price")
    private double lowPrice;

    @Column(name = "volume")
    private double volume;
}
