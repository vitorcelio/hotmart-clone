package com.hotmart.products.repositories;

import com.hotmart.products.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByUserId(Long userId);

    List<Product> findByUserIdAndCategoryId(Long userId, Integer categoryId);

}
