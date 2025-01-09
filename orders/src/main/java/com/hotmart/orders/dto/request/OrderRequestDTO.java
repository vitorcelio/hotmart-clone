package com.hotmart.orders.dto.request;

import jakarta.validation.Valid;
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
public class OrderRequestDTO {

    @NotNull
    private Long productId;
    @NotNull
    private BigDecimal price;
    @Valid
    private BuyerOrderRequestDTO buyer;
    @Valid
    private AddressOrderRequestDTO address;

}
