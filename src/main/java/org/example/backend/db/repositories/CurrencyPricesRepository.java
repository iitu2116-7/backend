package org.example.backend.db.repositories;

import org.example.backend.db.entites.CurrencyPrices;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CurrencyPricesRepository extends JpaRepository<CurrencyPrices, Long> {
    @Query("SELECT er FROM CurrencyPrices er ORDER BY er.id ASC")
    CurrencyPrices findFirstCurrencyPrice();

    @Query("SELECT CASE :currencyCode " +
            "WHEN 'CNY' THEN c.cny " +
            "WHEN 'EUR' THEN c.eur " +
            "WHEN 'GBP' THEN c.gbp " +
            "WHEN 'KGS' THEN c.kgs " +
            "WHEN 'RUB' THEN c.rub " +
            "WHEN 'USD' THEN c.usd " +
            "WHEN 'KZT' THEN 1.0 " +
            "ELSE NULL END " +
            "FROM CurrencyPrices c")
    Optional<Double> findExchangeRateByCurrencyCode(String currencyCode);
}
