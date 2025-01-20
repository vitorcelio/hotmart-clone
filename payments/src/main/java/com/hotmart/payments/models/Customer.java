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
    
    @Column(name = "user_id", nullable = false)
    private Long userId;
    
    @Column(nullable = false)
    private String email;
    
    @Column(name = "integration_id", nullable = false)
    private String integrationId;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentGateway gateway;
    
}
