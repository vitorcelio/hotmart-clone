package com.hotmart.orders.documents;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Transient;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentOrder {

    private Long id;
    private String type;
    private Integer installment;
    private String lastDigitsCardNumber;
    private BigDecimal price;
    private BigDecimal totalPrice;

    @Transient
    private String cardNumber;
    @Transient
    private String yearExpiry;
    @Transient
    private String monthExpiry;
    @Transient
    private String cardholderName;
    @Transient
    private String cvv;

}
