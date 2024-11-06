package org.example.backend.db.repositories;

import jakarta.transaction.Transactional;
import org.example.backend.db.entites.VerificationCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface VerificationCodeRepository extends JpaRepository<VerificationCode, Long> {

    @Query("SELECT CASE WHEN COUNT(s) > 0 THEN true ELSE false END FROM VerificationCode s WHERE s.username = :username AND s.code = :code")
    boolean existsByUsernameAndCode(@Param("username")String username, @Param("code") String code);

    @Modifying
    @Transactional
    @Query("DELETE FROM VerificationCode v WHERE v.username = :username AND v.code = :code")
    void deleteByUsernameAndCode(@Param("username") String username, @Param("code") String code);
}
