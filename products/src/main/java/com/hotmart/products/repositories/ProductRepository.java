package com.hotmart.products.repositories;

import com.hotmart.products.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByUserId(Long userId);

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


}
