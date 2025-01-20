package com.hotmart.products.dto.event;

import com.hotmart.products.enums.ProductType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductOrderDTO {

    private Long id;
    private Long planId;
    private String name;
    private ProductType type;
    private BigDecimal price;

}
