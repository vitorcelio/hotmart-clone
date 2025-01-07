package com.hotmart.products.repositories;

import com.hotmart.products.models.Affiliate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface AffiliateRepository extends JpaRepository<Affiliate, Long> {

    Optional<Affiliate> findByUserId(Long userId);

    @Query(value = """
        SELECT * FROM affiliates a, affiliate_product ap WHERE a.id = ap.affiliate_id
            AND ap.pending = false
            AND ap.product_id = :productId
            AND (:email IS NULL OR LOWER(a.user_email) LIKE LOWER(CONCAT('%', :email, '%')))
            AND (:name IS NULL OR LOWER(a.user_name) LIKE LOWER(CONCAT('%', :name, '%')))
    """, nativeQuery = true)
    List<Affiliate> findAllByProductId(
            @Param("productId") Long productId,
            @Param("email") String email,
            @Param("name") String name
    );

    @Query(value = """
        SELECT * FROM affiliates a, affiliate_product ap WHERE a.id = ap.affiliate_id
            AND ap.pending = true
            AND ap.product_id = :productId
            AND (:email IS NULL OR LOWER(a.user_email) LIKE LOWER(CONCAT('%', :email, '%')))
            AND (:name IS NULL OR LOWER(a.user_name) LIKE LOWER(CONCAT('%', :name, '%')))
    """, nativeQuery = true)
    List<Affiliate> findAllPendingByProductId(
            @Param("productId") Long productId,
            @Param("email") String email,
            @Param("name") String name
    );

    @Transactional
    @Modifying
    @Query("DELETE FROM AffiliateProduct ap WHERE ap.productId = :productId")
    void deleteAllByProductId(Long productId);

}
