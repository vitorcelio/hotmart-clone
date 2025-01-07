package com.hotmart.products.repositories;

import com.hotmart.products.models.Plan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlanRepository extends JpaRepository<Plan, Long> {

    List<Plan> findAllByProductId(Long productId);

    @Transactional
    @Modifying
    @Query("DELETE FROM Plan p WHERE p.productId = :productId")
    void deleteAllByProductId(@Param("productId") Long productId);

    @Query("""
        SELECT p FROM Plan p, BuyerProduct bp WHERE p.productId = :productId
            AND p.productId = bp.productId
            AND p.id = bp.planId
            AND bp.buyerId = :buyerId
    """)
    Optional<Plan> findByProductIdAndBuyerId(@Param("productId") Long productId, @Param("buyerId") Long buyerId);

}
