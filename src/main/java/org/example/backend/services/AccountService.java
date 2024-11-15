package org.example.backend.services;

import jakarta.transaction.Transactional;

import java.math.BigDecimal;

public interface AccountService {
    @Transactional
    void depositToAccount(Long customerId, BigDecimal amount);
}
