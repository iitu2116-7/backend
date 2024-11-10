package org.example.backend.db.repositories;

import org.example.backend.db.entites.CryptoPrices;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CryptoPricesRepository extends JpaRepository<CryptoPrices, Long> {
    CryptoPrices findBySymbol(String symbol);
}
