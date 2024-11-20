package org.example.backend.db.entites;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.backend.db.BaseEntity;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "notifications")
public class Notification extends BaseEntity {
    @Column(name = "message")
    private String message;

    @JoinColumn(name = "customer")
    @ManyToOne
    private Customer customer;

    @Column(name = "is_read")
    private boolean isRead;
}
