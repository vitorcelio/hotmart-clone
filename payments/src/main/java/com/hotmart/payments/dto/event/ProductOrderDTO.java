package com.hotmart.payments.dto.event;

import com.hotmart.payments.enums.PaymentMethod;
import com.hotmart.payments.enums.PeriodicitySubscription;
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
    private String type;
    private BigDecimal price;
    private PaymentMethod method;
    private PeriodicitySubscription periodicity;

}
