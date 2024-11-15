package org.example.backend.controllers;

import org.example.backend.db.enums.Currency;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class EnumController {

    @GetMapping("/enum/currencies")
    public ResponseEntity<List<String>> getAllCurrencies() {
        List<String> currencies = Arrays.stream(Currency.values())
                .map(Enum::name)
                .collect(Collectors.toList());
        return ResponseEntity.ok(currencies);
    }
}
