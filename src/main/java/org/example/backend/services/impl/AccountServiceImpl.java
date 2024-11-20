package org.example.backend.services.impl;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.example.backend.db.entites.Account;
import org.example.backend.db.entites.Customer;
import org.example.backend.db.entites.Notification;
import org.example.backend.db.entites.Transaction;
import org.example.backend.db.enums.TransactionType;
import org.example.backend.db.repositories.*;
import org.example.backend.services.AccountService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;

@Service
@AllArgsConstructor
public class AccountServiceImpl implements AccountService {
    private final AccountRepository accountRepository;
    private final CustomerRepository customerRepository;
    private final CurrencyPricesRepository currencyPricesRepository;
    private final TransactionRepository transactionRepository;
    private final NotificationRepository notificationRepository;

    @Transactional
    @Override
    public void depositToAccount(Long customerId, BigDecimal amount) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer not found"));

        Account account = accountRepository.findByCustomer(customer)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found for customer"));

        String preferredCurrency = customer.getPreferredCurrency().name();

        BigDecimal exchangeRate = getExchangeRate(preferredCurrency);
        BigDecimal amountInKZT = amount.multiply(exchangeRate);

        account.setBalanceInKZT(account.getBalanceInKZT().add(amountInKZT));
        accountRepository.save(account);

        Transaction transaction = new Transaction(customer, account, amountInKZT, TransactionType.DEPOSIT);
        transactionRepository.save(transaction);

        String notificationMessage = String.format(
                "Your account has been successfully credited with %.2f %s. Thank you for using our service!",
                amount,
                customer.getPreferredCurrency().name()
        );

        Notification notification = new Notification(notificationMessage, customer, false);
        notificationRepository.save(notification);
    }

    private BigDecimal getExchangeRate(String currencyCode) {
        return currencyPricesRepository.findExchangeRateByCurrencyCode(currencyCode.toUpperCase())
                .map(BigDecimal::valueOf)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unsupported currency: " + currencyCode));
    }

}
