package com.hotmart.orders.documents;

import com.hotmart.orders.enums.PaymentType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentOrder {

    private PaymentType type;
    private Integer installment;
    private String lastDigitsCardNumber;
    private BigDecimal price;

}
