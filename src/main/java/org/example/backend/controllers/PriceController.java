package org.example.backend.controllers;

import lombok.AllArgsConstructor;
import org.example.backend.db.entites.CurrencyPrices;
import org.example.backend.dto.dtos.CryptoPricesDTO;
import org.example.backend.services.utilServices.CryptoPricesService;
import org.example.backend.services.utilServices.CurrencyPricesService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class PriceController {
    private final CurrencyPricesService currencyPricesService;
    private final CryptoPricesService cryptoPricesService;

    @GetMapping("/current/currency-prices")
    public ResponseEntity<?> getCurrentExchangeRate() {
        CurrencyPrices currencyPrices = currencyPricesService.getCurrencyPrices();
        if (currencyPrices != null) {
            return ResponseEntity.ok(currencyPrices);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Exchange rate data not found");
        }
    }

    @GetMapping("/current/crypto-currency-prices")
    public ResponseEntity<Page<CryptoPricesDTO>> getCryptoPricesByFilter(
            @RequestParam(required = false, defaultValue = "popular") String filter,
            Pageable pageable) {
        Page<CryptoPricesDTO> result = cryptoPricesService.getCurrentCryptoPricesByFilter(filter, pageable);
        return ResponseEntity.ok(result);
    }
}
