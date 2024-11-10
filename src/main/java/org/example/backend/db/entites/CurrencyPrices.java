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
    private double usd;
    private double cny;
    private double eur;
    private double kgs;
    private double gbp;
    private double rub;
}
