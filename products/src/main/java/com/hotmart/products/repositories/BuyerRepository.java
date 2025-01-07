package com.hotmart.products.repositories;

import com.hotmart.products.models.Buyer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface BuyerRepository extends JpaRepository<Buyer, Long> {

    @Query(value = """
        SELECT * FROM buyers b, buyers_products bp WHERE b.id = bp.buyer_id
            AND bp.product_id = :productId
            AND (:email IS NULL OR LOWER(b.user_email) LIKE LOWER(CONCAT('%', :email, '%')))
            AND (:name IS NULL OR LOWER(b.user_name) LIKE LOWER(CONCAT('%', :name, '%')))
    """, nativeQuery = true)
    List<Buyer> findAllByProductId(
            @Param("productId") Long productId,
            @Param("email") String email,
            @Param("name") String name
    );

    @Transactional
    @Modifying
    @Query("DELETE FROM BuyerProduct bp WHERE bp.productId = :productId")
    void deleteAllByProductId(Long productId);

    Optional<Buyer> findByUserId(Long userId);

    Optional<Buyer> findByEmail(String email);
}
