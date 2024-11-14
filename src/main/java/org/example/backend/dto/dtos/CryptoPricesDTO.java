package org.example.backend.dto.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class CryptoPricesDTO {
    private Long id;
    private String symbol;
    private String name;
    private Double lastPrice;
    private Double volume;
    private Double priceChange;
    private Double priceChangePercent;
    private Double highPrice;
    private Double lowPrice;
    private Date lastUpdate;
}
