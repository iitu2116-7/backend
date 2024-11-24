package org.example.backend.services.impl;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.example.backend.db.entites.*;
import org.example.backend.db.enums.TransactionType;
import org.example.backend.db.repositories.*;
import org.example.backend.dto.dtos.AccountDTO;
import org.example.backend.services.AccountService;
import org.example.backend.utils.AccountMapper;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.List;

@Service
@AllArgsConstructor
public class AccountServiceImpl implements AccountService {
    private final AccountRepository accountRepository;
    private final CustomerRepository customerRepository;
    private final CurrencyPricesRepository currencyPricesRepository;
    private final TransactionRepository transactionRepository;
    private final NotificationRepository notificationRepository;
    private final AssetAccountRepository assetAccountRepository;
    private final CryptoPricesRepository cryptoPricesRepository;
    private final AccountMapper accountMapper;

    @Override
    public AccountDTO getAccount(Long customerId) {
        BigDecimal balanceInKzt = accountRepository.findBalanceByCustomerId(customerId);
        List<AssetAccount> assets = assetAccountRepository.findAllByCustomerId(customerId);
        return accountMapper.toDto(balanceInKzt, assets);
    }

    @Transactional
    @Override
    public void depositToAccount(Long customerId, BigDecimal amount) {
        processAccountTransaction(customerId, amount, TransactionType.DEPOSIT);
    }

    @Transactional
    @Override
    public void withdrawFromAccount(Long customerId, BigDecimal amount) {
        processAccountTransaction(customerId, amount, TransactionType.WITHDRAWAL);
    }

    @Transactional
    @Override
    public void purchaseCrypto(Long customerId, Long cryptoId, BigDecimal amount) {
        processCryptoTransaction(customerId, cryptoId, amount, TransactionType.PURCHASE);
    }

    @Transactional
    @Override
    public void sellCrypto(Long customerId, Long cryptoId, BigDecimal amount) {
        processCryptoTransaction(customerId, cryptoId, amount, TransactionType.SELL);
    }

    private void processAccountTransaction(Long customerId, BigDecimal amount, TransactionType transactionType) {
        Customer customer = getCustomerById(customerId);
        Account account = getAccountByCustomer(customer);

        BigDecimal amountInKZT = convertToKZT(amount, customer.getPreferredCurrency().name());

        if (transactionType == TransactionType.DEPOSIT) {
            account.setBalanceInKZT(account.getBalanceInKZT().add(amountInKZT));
        } else if (transactionType == TransactionType.WITHDRAWAL) {
            if (account.getBalanceInKZT().compareTo(amountInKZT) < 0) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Insufficient funds");
            }
            account.setBalanceInKZT(account.getBalanceInKZT().subtract(amountInKZT));
        }

        accountRepository.save(account);

        Transaction transaction = new Transaction(customer, account, amountInKZT, null, transactionType);
        transactionRepository.save(transaction);

        sendNotification(customer, amount, customer.getPreferredCurrency().name(), transactionType);
    }

    private void processCryptoTransaction(Long customerId, Long cryptoId, BigDecimal amount, TransactionType transactionType) {
        Customer customer = getCustomerById(customerId);
        Account account = getAccountByCustomer(customer);
        CryptoPrices crypto = getCryptoById(cryptoId);

        BigDecimal amountInKZT = convertToKZT(amount, customer.getPreferredCurrency().name());
        BigDecimal cryptoPrice = cryptoPricesRepository.getLastPrice(cryptoId);
        BigDecimal cryptoQuantity = calculateCryptoQuantity(amount, cryptoPrice);

        AssetAccount asset = assetAccountRepository.findByCustomerAndCrypto(customerId, cryptoId)
                .orElse(new AssetAccount(customer, crypto, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO));

        if (transactionType == TransactionType.SELL) {
            handleCryptoSell(account, asset, cryptoQuantity, cryptoPrice);
        } else if (transactionType == TransactionType.PURCHASE) {
            handleCryptoPurchase(account, asset, amountInKZT, cryptoQuantity);
        }

        Transaction transaction = new Transaction(customer, account, amountInKZT, cryptoQuantity, transactionType);
        transactionRepository.save(transaction);

        sendNotification(customer, amount, crypto.getName(), transactionType);
    }

    private Customer getCustomerById(Long customerId) {
        return customerRepository.findById(customerId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "NOT FOUND"));
    }

    private Account getAccountByCustomer(Customer customer) {
        return accountRepository.findByCustomer(customer)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "NOT FOUND"));
    }

    private CryptoPrices getCryptoById(Long cryptoId) {
        return cryptoPricesRepository.findById(cryptoId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "NOT FOUND"));
    }

    private BigDecimal convertToKZT(BigDecimal amount, String currencyCode) {
        BigDecimal exchangeRate = currencyPricesRepository.findExchangeRateByCurrencyCode(currencyCode.toUpperCase())
                .map(BigDecimal::valueOf)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unsupported currency: " + currencyCode));
        return amount.multiply(exchangeRate);
    }

    private BigDecimal calculateCryptoQuantity(BigDecimal amount, BigDecimal cryptoPrice) {
        return amount.divide(cryptoPrice, 8, RoundingMode.HALF_UP);
    }

    private void handleCryptoSell(Account account, AssetAccount asset, BigDecimal cryptoQuantity, BigDecimal cryptoPrice) {
        if (asset.getQuantity().compareTo(cryptoQuantity) < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Insufficient crypto quantity");
        }

        asset.setQuantity(asset.getQuantity().subtract(cryptoQuantity));
        asset.setUpdatedDate(new Date());
        assetAccountRepository.save(asset);

        BigDecimal amountInKZT = cryptoQuantity.multiply(cryptoPrice);
        account.setBalanceInKZT(account.getBalanceInKZT().add(amountInKZT));
        accountRepository.save(account);
    }

    private void handleCryptoPurchase(Account account, AssetAccount asset, BigDecimal amountInKZT, BigDecimal cryptoQuantity) {
        if (account.getBalanceInKZT().compareTo(amountInKZT) < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Insufficient funds");
        }

        account.setBalanceInKZT(account.getBalanceInKZT().subtract(amountInKZT));

        BigDecimal newQuantity = asset.getQuantity().add(cryptoQuantity);
        BigDecimal totalInvestment = asset.getPurchasedAmount().multiply(asset.getQuantity())
                .add(amountInKZT);
        BigDecimal averagePurchasePrice = totalInvestment.divide(newQuantity, 8, RoundingMode.HALF_UP);

        asset.setQuantity(newQuantity);
        asset.setPurchasedAmount(averagePurchasePrice);
        asset.setUpdatedDate(new Date());
        assetAccountRepository.save(asset);
    }

    private void sendNotification(Customer customer, BigDecimal amount, String identifier, TransactionType transactionType) {
        String action = transactionType == TransactionType.DEPOSIT || transactionType == TransactionType.PURCHASE ? "credited" : "debited";
        String notificationMessage = String.format(
                "Your account has been successfully %s with %.2f %s. Thank you for using our service!",
                action, amount, identifier
        );

        Notification notification = new Notification(notificationMessage, customer, false);
        notificationRepository.save(notification);
    }

//    @Scheduled(fixedRate = 3600000)
    @Scheduled(fixedRate = 60000)
    public void updateAssetProfit() {
        List<AssetAccount> assets = assetAccountRepository.findAll();
        for (AssetAccount asset : assets) {
            BigDecimal cryptoPrice = cryptoPricesRepository.getLastPrice(asset.getId());
            BigDecimal profit = cryptoPrice.subtract(asset.getPurchasedAmount()).multiply(asset.getQuantity());
            asset.setProfit(profit);
            asset.setUpdatedDate(new Date());
            assetAccountRepository.save(asset);
        }
    }
}
