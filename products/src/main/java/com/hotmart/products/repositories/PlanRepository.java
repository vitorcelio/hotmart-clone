package com.hotmart.products.repositories;

import com.hotmart.products.models.Plan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface PlanRepository extends JpaRepository<Plan, Long> {

    List<Plan> findAllByProductId(Long productId);

    @Transactional
    @Modifying
    @Query("DELETE FROM Plan p WHERE p.productId = :productId")
    void deleteAllByProductId(@Param("productId") Long productId);

}
