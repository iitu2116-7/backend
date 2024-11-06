package org.example.backend.db.repositories;

import org.example.backend.db.entites.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    boolean existsByUsername(String email);

    @Query("SELECT c FROM Customer c WHERE c.username = :username")
    Optional<Customer> findByUsernameOptional(String username);

    Customer findByUsername(String phone);
}
