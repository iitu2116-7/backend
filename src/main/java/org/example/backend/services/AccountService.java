package org.example.backend.services;

import jakarta.transaction.Transactional;
import org.example.backend.dto.dtos.AccountDTO;

import java.math.BigDecimal;

public interface AccountService {
    AccountDTO getAccount(Long customerId);

    @Transactional
    void depositToAccount(Long customerId, BigDecimal amount);

    @Transactional
    void withdrawFromAccount(Long customerId, BigDecimal amount);

    @Transactional
    void purchaseCrypto(Long customerId, Long cryptoId, BigDecimal amount);

    @Transactional
    void sellCrypto(Long customerId, Long cryptoId, BigDecimal amount);
}
