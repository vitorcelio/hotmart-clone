package com.hotmart.orders.dto.request;

import com.hotmart.orders.documents.ProductOrder;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductOrderRequestDTO {

    @NotNull(message = "O produto é obrigatório")
    private Long id;

    private Long planId;

    @NotNull(message = "O preço é obrigatório")
    private BigDecimal price;

    @NotNull(message = "O tipo do produto é obrigatório")
    private String type;

    public ProductOrder toProductOrder() {
        return ProductOrder.builder()
                .id(id)
                .planId(planId)
                .price(price)
                .type(type)
                .build();
    }

}
