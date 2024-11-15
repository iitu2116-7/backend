package org.example.backend.controllers;

import lombok.AllArgsConstructor;
import org.example.backend.services.AccountService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@AllArgsConstructor
public class AccountController extends BaseController {

    private final AccountService accountService;

    @PostMapping("/deposit")
    public ResponseEntity<String> deposit(@RequestParam BigDecimal amount) {
        Long customerId = Long.valueOf(data.get("id"));
        accountService.depositToAccount(customerId, amount);
        return ResponseEntity.ok("Deposit successful");
    }
}

