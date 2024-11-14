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

    @Column(name = "name")
    private String name;

    @Column(name = "last_price")
    private Double lastPrice;

    @Column(name = "volume")
    private Double volume;

    @Column(name = "price_change")
    private Double priceChange;

    @Column(name = "price_change_percent")
    private Double priceChangePercent;

    @Column(name = "high_price")
    private Double highPrice;

    @Column(name = "low_price")
    private Double lowPrice;
}
