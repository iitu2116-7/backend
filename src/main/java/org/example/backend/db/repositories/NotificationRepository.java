package org.example.backend.db.repositories;

import org.example.backend.db.entites.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    @Query("SELECT n FROM Notification n WHERE n.customer.id = :customerId ORDER BY n.createdDate DESC")
    List<Notification> findByCustomerId(Long customerId);

}
