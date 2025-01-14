package com.hotmart.orders.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderRequestDTO {

    @Valid
    private ProductOrderRequestDTO product;

    @Valid
    private BuyerOrderRequestDTO buyer;

    @Valid
    private AddressOrderRequestDTO address;

    @Valid
    private PaymentOrderRequestDTO payment;

}
