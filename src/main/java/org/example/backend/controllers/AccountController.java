package org.example.backend.controllers;

import lombok.AllArgsConstructor;
import org.example.backend.dto.dtos.AccountDTO;
import org.example.backend.services.AccountService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@AllArgsConstructor
@RequestMapping("/account")
public class AccountController extends BaseController {

    private final AccountService accountService;

    @GetMapping
    public ResponseEntity<AccountDTO> getAccount() {
        Long customerId = Long.valueOf(data.get("id"));
        accountService.getAccount(customerId);
        return ResponseEntity.ok(accountService.getAccount(customerId));
    }

    @PostMapping("/deposit")
    public ResponseEntity<String> deposit(@RequestParam BigDecimal amount) {
        Long customerId = Long.valueOf(data.get("id"));
        accountService.depositToAccount(customerId, amount);
        return ResponseEntity.ok("Deposit successful");
    }

    @PostMapping("/withdraw")
    public ResponseEntity<String> withdrawFromAccount(@RequestParam BigDecimal amount) {
        Long customerId = Long.valueOf(data.get("id"));
        accountService.withdrawFromAccount(customerId, amount);
        return ResponseEntity.ok("Withdrawal successful");
    }

    @PostMapping("/crypto/{cryptoId}/purchase")
    public ResponseEntity<String> purchaseCrypto(@PathVariable Long cryptoId, @RequestParam BigDecimal amount) {
        Long customerId = Long.valueOf(data.get("id"));
        accountService.purchaseCrypto(customerId, cryptoId, amount);
        return ResponseEntity.ok("Crypto purchase successful");
    }

    @PostMapping("/crypto/{cryptoId}/sell")
    public ResponseEntity<String> sellCrypto(@PathVariable Long cryptoId, @RequestParam BigDecimal amount) {
        Long customerId = Long.valueOf(data.get("id"));
        accountService.sellCrypto(customerId, cryptoId, amount);
        return ResponseEntity.ok("Crypto sale successful");
    }
}

