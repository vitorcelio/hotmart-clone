package com.hotmart.products.repositories;

import com.hotmart.products.models.BuyerProduct;
import com.hotmart.products.models.pk.BuyerProductPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface BuyerProductRepository extends JpaRepository<BuyerProduct, BuyerProductPK> {

    boolean existsBuyerProductByBuyer_UserIdAndProductId(Long userId, Long productId);

    @Transactional
    @Modifying
    @Query("DELETE FROM BuyerProduct bp WHERE bp.buyerId = :buyerId AND bp.productId = :productId")
    void deleteBuyerProduct(Long buyerId, Long productId);

    Optional<BuyerProduct> findByBuyerIdAndProductId(Long buyerId, Long productId);

}
