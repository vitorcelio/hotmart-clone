package com.hotmart.products.models;

import com.hotmart.products.models.pk.BuyerProductPK;
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
@Table(name = "buyers_products")
@IdClass(BuyerProductPK.class)
public class BuyerProduct {

    @Id
    @Column(name = "buyer_id", nullable = false)
    private Long buyerId;

    @Id
    @Column(name = "product_id", nullable = false)
    private Long productId;

    @Column(name = "plan_id")
    private Long planId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "buyer_id", referencedColumnName = "id", insertable = false, updatable = false)
    private Buyer buyer;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id", referencedColumnName = "id", insertable = false, updatable = false)
    private Product product;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "plan_id", referencedColumnName = "id", insertable = false, updatable = false)
    private Plan plan;

}
