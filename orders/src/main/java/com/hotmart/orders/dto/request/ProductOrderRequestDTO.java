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

    @NotNull(message = "O preço é obrigatório")
    private BigDecimal price;

    public ProductOrder toProductOrder() {
        return ProductOrder.builder()
                .id(id)
                .price(price)
                .build();
    }

}
