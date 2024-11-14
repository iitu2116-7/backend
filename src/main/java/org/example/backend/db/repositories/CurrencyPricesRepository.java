package org.example.backend.db.repositories;

import org.example.backend.db.entites.CurrencyPrices;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CurrencyPricesRepository extends JpaRepository<CurrencyPrices, Long> {
    @Query("SELECT er FROM CurrencyPrices er ORDER BY er.id ASC")
    CurrencyPrices findFirstCurrencyPrice();
}
