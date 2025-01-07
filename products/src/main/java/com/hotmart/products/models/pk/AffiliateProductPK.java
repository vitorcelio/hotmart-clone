package com.hotmart.products.models.pk;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode
public class AffiliateProductPK {

    @Id
    @Column(name = "affiliate_id", nullable = false)
    private Long affiliateId;

    @Id
    @Column(name = "product_id", nullable = false)
    private Long productId;

}
