package com.hotmart.products.repositories;

import com.hotmart.products.models.Validation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ValidationRepository extends JpaRepository<Validation, Long> {

    Optional<Validation> findByOrderIdAndTransactionId(String orderId, String transactionId);

    boolean existsByOrderIdAndTransactionId(String orderId, String transactionId);

}
