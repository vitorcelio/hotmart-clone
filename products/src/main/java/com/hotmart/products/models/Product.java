package com.hotmart.products.models;

import com.hotmart.products.dto.response.PlanResponseDTO;
import com.hotmart.products.enums.PaymentMethod;
import com.hotmart.products.enums.ProductType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

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

    @Column(columnDefinition = "character varying(250)", nullable = false)
    private String name;

    @Column(columnDefinition = "character varying(2000)", nullable = false)
    private String description;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProductType type;

    @ManyToOne
    @JoinColumn(name = "category_id", referencedColumnName = "id", insertable = false, updatable = false)
    private Category category;

    @Column(name = "category_id", nullable = false)
    private Integer categoryId;

    @Column(name = "day_refund_request", nullable = false)
    private Integer dayRefundRequest;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method")
    private PaymentMethod paymentMethod;

    @Column(precision = 19, scale = 2)
    private BigDecimal price;

    @ColumnDefault(value = "false")
    @Column(nullable = false)
    private boolean affiliation;

    @Column(columnDefinition = "TEXT")
    private String image;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Transient
    private List<PlanResponseDTO> plans;

    @PrePersist
    public void prePersist() {
        var now = LocalDateTime.now();
        this.createdAt = now;
        this.updatedAt = now;
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

}