package org.example.backend.db.repositories;

import org.example.backend.db.entites.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    @Query("SELECT t FROM Transaction t " +
            "WHERE t.customer.id = :customerId " +
            "AND CAST(t.createdDate AS date) BETWEEN :dateFrom AND :dateTo " +
            "ORDER BY t.createdDate DESC")
    Page<Transaction> findByCustomerIdAndDateRange(Long customerId, Date dateFrom, Date dateTo, Pageable pageable);

}
