package org.example.backend.db.repositories;

import org.example.backend.db.entites.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    boolean existsByEmail(String email);

    @Query("SELECT c FROM Customer c WHERE c.email = :email")
    Optional<Customer> findByEmailOptional(String email);

    @Query("SELECT c FROM Customer c WHERE c.email = :email")
    Customer findByEmail(String email);
}
