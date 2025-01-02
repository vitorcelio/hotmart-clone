package com.hotmart.products.models;

import com.hotmart.products.enums.BillingMethod;
import com.hotmart.products.enums.PaymentMethod;
import com.hotmart.products.enums.PeriodicitySubscription;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "plans")
public class Plan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "product_id", referencedColumnName = "id", insertable = false, updatable = false)
    private Product product;

    @Column(name = "product_id", nullable = false)
    private Long productId;

    @Column(nullable = false, columnDefinition = "character varying(50)")
    private String name;

    @Column(columnDefinition = "character varying(250)")
    private String description;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal price;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PeriodicitySubscription periodicity;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentMethod paymentMethod;

    @Column(nullable = false)
    private Integer installment;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BillingMethod billingMethod;

    @Column(nullable = false)
    private Integer recurrences;

}
