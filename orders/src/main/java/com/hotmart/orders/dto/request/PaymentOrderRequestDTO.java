package com.hotmart.orders.dto.request;

import com.hotmart.orders.documents.PaymentOrder;
import com.hotmart.orders.enums.PaymentType;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.ObjectUtils;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentOrderRequestDTO {

    @NotNull
    private String type;
    private String cardNumber;
    private String yearExpiry;
    private String monthExpiry;
    private String cardholderName;
    private Integer installment;
    private BigDecimal price;
    private BigDecimal totalPrice;

    public boolean validation() {
        if (type.equals("CREDIT_CARD")) {
            return !ObjectUtils.isEmpty(cardNumber) && !ObjectUtils.isEmpty(yearExpiry) && !ObjectUtils.isEmpty(monthExpiry) && !ObjectUtils.isEmpty(cardholderName) && !ObjectUtils.isEmpty(installment);
        }

        return true;
    }

    public PaymentOrder toPaymentOrder() {
        return PaymentOrder.builder()
                .type(type)
                .installment(installment)
                .price(price)
                .totalPrice(totalPrice)
                .build();
    }

}
