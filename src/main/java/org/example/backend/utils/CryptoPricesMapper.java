package org.example.backend.utils;

import org.example.backend.db.entites.CryptoPrices;
import org.example.backend.dto.dtos.CryptoPricesDTO;
import org.springframework.stereotype.Component;

@Component
public class CryptoPricesMapper {

    public CryptoPricesDTO toDto(CryptoPrices cryptoPrices) {
        return new CryptoPricesDTO(
                cryptoPrices.getId(),
                cryptoPrices.getSymbol(),
                cryptoPrices.getName(),
                cryptoPrices.getLastPrice(),
                cryptoPrices.getVolume(),
                cryptoPrices.getPriceChange(),
                cryptoPrices.getPriceChangePercent(),
                cryptoPrices.getHighPrice(),
                cryptoPrices.getLowPrice(),
                cryptoPrices.getUpdateDate()
        );
    }

}
