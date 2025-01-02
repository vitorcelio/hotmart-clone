package com.hotmart.products.dto.response;

import com.hotmart.products.models.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryResponseDTO {

    private Integer id;
    private String name;

    public CategoryResponseDTO(Category category) {
        this.id = category.getId();
        this.name = category.getName();
    }

    public static List<CategoryResponseDTO> convert(List<Category> categories) {
        return categories.stream().map(CategoryResponseDTO::new).collect(Collectors.toList());
    }

}
