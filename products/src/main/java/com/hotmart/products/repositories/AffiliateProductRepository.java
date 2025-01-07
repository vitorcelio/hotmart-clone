package com.hotmart.products.repositories;

import com.hotmart.products.models.AffiliateProduct;
import com.hotmart.products.models.pk.AffiliateProductPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface AffiliateProductRepository extends JpaRepository<AffiliateProduct, AffiliateProductPK> {

    boolean existsAffiliateProductByAffiliateIdAndProductId(Long affiliateId, Long productId);

    @Transactional
    @Modifying
    @Query("DELETE FROM AffiliateProduct ap WHERE ap.affiliateId = :affiliateId AND ap.productId = :productId")
    void deleteAffiliateProduct(Long affiliateId, Long productId);

    Optional<AffiliateProduct> findByAffiliateIdAndProductId(Long affiliateId, Long productId);
}
