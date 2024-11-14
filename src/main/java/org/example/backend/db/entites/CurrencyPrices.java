package org.example.backend.db.entites;

import jakarta.persistence.*;
import lombok.*;
import org.example.backend.db.BaseEntity;

@Entity
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "currency_prices")
public class CurrencyPrices extends BaseEntity {

    @Column(name = "usd")
    private double usd;

    @Column(name = "cny")
    private double cny;

    @Column(name = "eur")
    private double eur;

    @Column(name = "kgs")
    private double kgs;

    @Column(name = "gbp")
    private double gbp;

    @Column(name = "rub")
    private double rub;
}
