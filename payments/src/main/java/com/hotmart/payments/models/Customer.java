package com.hotmart.payments.models;

import com.hotmart.payments.enums.PaymentGateway;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "customers")
public class Customer {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "integration_id", unique = true)
    private String integrationId;

    @Column(name = "user_id", nullable = false, unique = true)
    private Long userId;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(name = "api_key", nullable = false)
    private String apiKey;

    @Column(name = "wallet_id", unique = true)
    private String walletId;

    @Enumerated(EnumType.STRING)
    private PaymentGateway gateway;
    
}
