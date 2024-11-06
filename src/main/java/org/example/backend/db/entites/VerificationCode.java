package org.example.backend.db.entites;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.example.backend.db.BaseEntity;

@Entity
@SuperBuilder
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "verification_code")
public class VerificationCode extends BaseEntity {
    private int code;
    private String username;
}
