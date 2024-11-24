package org.example.backend.db.repositories;

import org.example.backend.db.entites.CryptoPrices;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface CryptoPricesRepository extends JpaRepository<CryptoPrices, Long> {

    @Query("SELECT c FROM CryptoPrices c WHERE c.symbol IN :symbols")
    Page<CryptoPrices> findAllBySymbolIn(List<String> symbols, Pageable pageable);

    @Query("SELECT c.lastPrice FROM CryptoPrices c WHERE c.id = :cryptoId")
    BigDecimal getLastPrice(Long cryptoId);
}
