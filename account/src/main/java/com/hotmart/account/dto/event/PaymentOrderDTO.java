package com.hotmart.account.dto.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentOrderDTO {

    private String type;
    private Integer installment;
    private BigDecimal price;
    private String lastDigitsCardNumber;

}
