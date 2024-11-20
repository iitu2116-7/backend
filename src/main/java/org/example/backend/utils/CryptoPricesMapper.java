package org.example.backend.utils;

import org.example.backend.db.entites.CryptoPrices;
import org.example.backend.dto.dtos.CryptoDTO;
import org.springframework.stereotype.Component;

@Component
public class CryptoPricesMapper {

    public CryptoDTO toDto(CryptoPrices cryptoPrices) {
        return new CryptoDTO(
                cryptoPrices.getId(),
                cryptoPrices.getSymbol(),
                cryptoPrices.getName(),
                cryptoPrices.getLastPrice(),
                cryptoPrices.getVolume(),
                cryptoPrices.getPriceChange(),
                cryptoPrices.getPriceChangePercent(),
                cryptoPrices.getHighPrice(),
                cryptoPrices.getLowPrice(),
                cryptoPrices.getUpdatedDate()
        );
    }

}
