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

    private Long id;
    private String type;
    private Integer installment;
    private String lastDigitsCardNumber;
    private BigDecimal price;
    private BigDecimal totalPrice;
    private String cardNumber;
    private String yearExpiry;
    private String monthExpiry;
    private String cardholderName;
    private String cvv;

}
