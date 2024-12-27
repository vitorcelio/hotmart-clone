package com.hotmart.auth.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "data_access")
public class DataAccessEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    private UserEntity user;

    @Column(nullable = false)
    private boolean downloaded;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "downloaded_at")
    private LocalDateTime downloadedAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        this.downloaded = false;
    }

    @PreUpdate
    public void preUpdate() {
        this.downloadedAt = LocalDateTime.now();
    }
}
