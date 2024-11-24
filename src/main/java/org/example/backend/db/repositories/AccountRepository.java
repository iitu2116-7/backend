package org.example.backend.db.repositories;

import org.example.backend.db.entites.Account;
import org.example.backend.db.entites.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findByCustomer(Customer customer);

    @Query("SELECT a.balanceInKZT FROM Account a WHERE a.customer.id = :customerId")
    BigDecimal findBalanceByCustomerId(Long customerId);
}
