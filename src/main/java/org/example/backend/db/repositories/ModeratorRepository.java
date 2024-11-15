package org.example.backend.db.repositories;

import org.example.backend.db.entites.Moderator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ModeratorRepository extends JpaRepository<Moderator, Long> {

    @Query("SELECT m FROM Moderator m WHERE m.email = :email")
    Moderator findByEmail(String email);
}
