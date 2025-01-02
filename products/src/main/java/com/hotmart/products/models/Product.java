package com.hotmart.products.models;

import com.hotmart.products.enums.PaymentMethod;
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
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 250, nullable = false)
    private String name;

    @Column(length = 2000, nullable = false)
    private String description;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @ManyToOne
    @JoinColumn(name = "category_id", referencedColumnName = "id", insertable = false, updatable = false)
    private Category category;

    @Column(name = "category_id", nullable = false)
    private Integer categoryId;

    @Column(name = "day_refund_request", nullable = false)
    private Integer dayRefundRequest;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method", nullable = false)
    private PaymentMethod paymentMethod;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal price;

}
