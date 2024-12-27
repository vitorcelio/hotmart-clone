package com.hotmart.account.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Random;
import java.util.UUID;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "recover_password")
public class RecoverPassword {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String pswdrst;

    @ManyToOne
    private User user;

    @Column(nullable = false)
    private boolean updated;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;


    @PrePersist
    protected void onCreate() {
        if (this.pswdrst == null) {
            String prefix = "TST-";
            String code = String.format("%05d", new Random().nextInt(100000)) + "-";
            this.pswdrst = prefix + code + UUID.randomUUID();
        }

        this.updated = false;
        this.createdAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updated = true;
        this.updatedAt = LocalDateTime.now();
    }

}
