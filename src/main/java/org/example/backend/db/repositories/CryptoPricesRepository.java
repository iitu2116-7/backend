package org.example.backend.db.repositories;

import org.example.backend.db.entites.CryptoPrices;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CryptoPricesRepository extends JpaRepository<CryptoPrices, Long> {
    CryptoPrices findBySymbol(String symbol);

    @Query("SELECT c FROM CryptoPrices c WHERE c.symbol IN :symbols")
    Page<CryptoPrices> findAllBySymbolIn(@Param("symbols") List<String> symbols, Pageable pageable);
}
