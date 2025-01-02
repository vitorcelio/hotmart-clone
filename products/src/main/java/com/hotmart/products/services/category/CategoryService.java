package com.hotmart.products.services.category;

import com.hotmart.products.dto.request.CategoryRequestDTO;
import com.hotmart.products.dto.response.CategoryResponseDTO;
import lombok.NonNull;

import java.util.List;

public interface CategoryService {

    CategoryResponseDTO save(@NonNull CategoryRequestDTO request);

    CategoryResponseDTO update(@NonNull Integer id, @NonNull CategoryRequestDTO request);

    List<CategoryResponseDTO> findAll(String name);

    CategoryResponseDTO findById(@NonNull Integer id);

    void delete(@NonNull Integer id);

}
