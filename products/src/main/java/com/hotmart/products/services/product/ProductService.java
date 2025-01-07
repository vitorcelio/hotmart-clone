package com.hotmart.products.services.product;

import com.hotmart.products.dto.request.PlanRequestDTO;
import com.hotmart.products.dto.request.ProductRequestDTO;
import com.hotmart.products.dto.response.PlanResponseDTO;
import com.hotmart.products.dto.response.ProductResponseDTO;
import lombok.NonNull;

import java.util.List;

public interface ProductService {

    ProductResponseDTO save(@NonNull ProductRequestDTO request);

    ProductResponseDTO update(@NonNull Long id, @NonNull ProductRequestDTO request);

    ProductResponseDTO findById(@NonNull Long id);

    List<ProductResponseDTO> findAll(String name, Integer categoryId);

    List<ProductResponseDTO> findAllBuyerProducts();

    List<ProductResponseDTO> findAllAffiliateProducts();

    PlanResponseDTO updatePlan(@NonNull Long id, @NonNull PlanRequestDTO request);

    void delete(@NonNull Long id);

    void deletePlan(@NonNull Long id);

    List<ProductResponseDTO> findAllProductsAffiliateRequest();

}
