package com.hotmart.payments.models;

import com.hotmart.payments.enums.PaymentGateway;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "api_keys")
public class ApiKeys {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID uuid;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentGateway gateway;

    @Column(name = "api_key")
    private String apiKey;

    @JoinColumn(name = "customer_id", insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Customer customer;

    @Column(name = "customer_id", nullable = false)
    private Long customerId;

}
