package com.hotmart.products.models;

import com.hotmart.products.models.pk.AffiliateProductPK;
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
@Table(name = "affiliate_product")
@IdClass(AffiliateProductPK.class)
public class AffiliateProduct {

    @Id
    @Column(name = "affiliate_id", nullable = false)
    private Long affiliateId;

    @Id
    @Column(name = "product_id", nullable = false)
    private Long productId;

    @Column(nullable = false)
    private boolean pending;

    @ManyToOne
    @JoinColumn(name = "affiliate_id", referencedColumnName = "id", insertable = false, updatable = false)
    private Affiliate affiliate;

    @ManyToOne
    @JoinColumn(name = "product_id", referencedColumnName = "id", insertable = false, updatable = false)
    private Product product;

}
