package org.example.backend.db.repositories;

import org.example.backend.db.entites.AssetAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AssetAccountRepository extends JpaRepository<AssetAccount, Long> {

    @Query("SELECT a FROM AssetAccount a WHERE a.customer.id = :customerId AND a.crypto.id = :cryptoId")
    Optional<AssetAccount> findByCustomerAndCrypto(Long customerId, Long cryptoId);

    @Query("SELECT a FROM AssetAccount a WHERE a.customer.id = :customerId")
    List<AssetAccount> findAllByCustomerId(Long customerId);
}
