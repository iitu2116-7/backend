package org.example.backend.services.utilServices;

import lombok.AllArgsConstructor;

import org.example.backend.db.entites.CryptoPrices;
import org.example.backend.db.repositories.CryptoPricesRepository;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@AllArgsConstructor
public class CryptoPricesService {

    private final CryptoPricesRepository cryptoPricesRepository;

    /**
     * Метод для обновления данных о криптовалюте
     * @param symbol Символ криптовалюты (например, "BTCUSDT")
     * @param price Последняя цена
     * @param openPrice Цена открытия
     * @param highPrice Максимальная цена за день
     * @param lowPrice Минимальная цена за день
     * @param volume Объем торгов
     */
    public void updateCryptoPrice(String symbol, double price, double openPrice, double highPrice, double lowPrice, double volume) {
        CryptoPrices cryptoPrice = cryptoPricesRepository.findBySymbol(symbol);

        if (cryptoPrice == null) {

            cryptoPrice = new CryptoPrices();
            cryptoPrice.setSymbol(symbol);
        }

        cryptoPrice.setPrice(price);
        cryptoPrice.setOpenPrice(openPrice);
        cryptoPrice.setHighPrice(highPrice);
        cryptoPrice.setLowPrice(lowPrice);
        cryptoPrice.setVolume(volume);
        cryptoPrice.setUpdateDate(new Date());

        cryptoPricesRepository.save(cryptoPrice);
    }
}