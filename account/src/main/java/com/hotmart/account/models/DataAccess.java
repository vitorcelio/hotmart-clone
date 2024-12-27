package com.hotmart.account.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "data_access")
public class DataAccess {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    private User user;

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
