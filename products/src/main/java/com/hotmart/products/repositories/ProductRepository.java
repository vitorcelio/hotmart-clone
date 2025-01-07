package com.hotmart.products.repositories;

import com.hotmart.products.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query(value = """
        SELECT * FROM products p
        WHERE (:userId IS NULL OR p.user_id = :userId)
          AND (:categoryId IS NULL OR p.category_id = :categoryId)
          AND (:name IS NULL OR LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%')))
    """, nativeQuery = true)
    List<Product> findAllProducts(
            @Param("userId") Long userId,
            @Param("categoryId") Integer categoryId,
            @Param("name") String name
    );

    @Query("""
        SELECT p FROM Product p, Buyer b, BuyerProduct bp WHERE p.id = bp.productId
            AND p.id = bp.productId
            AND b.id = bp.buyerId
            AND b.userId = :userId
    """)
    List<Product> findAllByBuyerUserId(@Param("userId") Long userId);

    @Query("""
        SELECT p FROM Product p, Affiliate a, AffiliateProduct ap WHERE p.id = ap.productId
            AND p.id = ap.productId
            AND a.id = ap.affiliateId
            AND ap.pending = false
            AND a.userId = :userId
    """)
    List<Product> findAllByAffiliateUserId(@Param("userId") Long userId);

    @Query("""
        SELECT p FROM Product p, Affiliate a, AffiliateProduct ap WHERE p.id = ap.productId
            AND p.id = ap.productId
            AND a.id = ap.affiliateId
            AND ap.pending = true
            AND a.userId = :userId
    """)
    List<Product> findAllPendingByAffiliateUserId(@Param("userId") Long userId);
}
