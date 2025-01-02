package com.hotmart.products.services.category.impl;

import com.hotmart.products.config.exception.ValidationException;
import com.hotmart.products.dto.request.CategoryRequestDTO;
import com.hotmart.products.dto.response.CategoryResponseDTO;
import com.hotmart.products.models.Category;
import com.hotmart.products.repositories.CategoryRepository;
import com.hotmart.products.services.category.CategoryService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository repository;

    @Override
    public CategoryResponseDTO save(@NonNull CategoryRequestDTO request) {
        validationNameCategory(request.getName(), null);

        Category category = Category.builder()
                .name(request.getName())
                .description(request.getDescription())
                .build();

        Category save = repository.save(category);

        return CategoryResponseDTO.builder()
                .id(save.getId())
                .name(save.getName())
                .description(save.getDescription())
                .build();
    }

    @Override
    public CategoryResponseDTO update(@NonNull Integer id, @NonNull CategoryRequestDTO request) {
        Category category = repository.findById(id).orElseThrow(() -> new ValidationException("Categoria não encontrada."));

        validationNameCategory(request.getName(), id);

        category.setName(request.getName());
        category.setDescription(request.getDescription());
        Category save = repository.save(category);

        return CategoryResponseDTO.builder()
                .id(save.getId())
                .name(save.getName())
                .description(save.getDescription())
                .build();
    }

    @Override
    public List<CategoryResponseDTO> findAll(String name) {
        List<CategoryResponseDTO> list;

        if (!ObjectUtils.isEmpty(name)) {
            list = CategoryResponseDTO.convert(repository.findAllByName(name));
        } else {
            list = CategoryResponseDTO.convert(repository.findAll());
        }

        return list;
    }

    @Override
    public CategoryResponseDTO findById(@NonNull Integer id) {
        Category category = repository.findById(id).orElseThrow(() -> new ValidationException("Categoria não encontrada."));
        return new CategoryResponseDTO(category);
    }

    @Override
    public void delete(@NonNull Integer id) {
        Category category = repository.findById(id).orElseThrow(() -> new ValidationException("Categoria não encontrada."));
        repository.delete(category);
    }

    private void validationNameCategory(@NonNull String name, Integer id) {
        Category category = repository.findByName(name).orElse(null);

        if (category == null) {
            return;
        }

        if (id == null) {
            throw new ValidationException("Já existe uma categoria com este nome.");
        }

        if (!category.getId().equals(id)) {
            throw new ValidationException("Já existe uma categoria com este nome.");
        }

    }


}
