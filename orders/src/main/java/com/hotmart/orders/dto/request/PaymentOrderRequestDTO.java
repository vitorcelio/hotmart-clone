package com.hotmart.orders.dto.request;

import com.hotmart.orders.documents.PaymentOrder;
import com.hotmart.orders.enums.PaymentType;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.ObjectUtils;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentOrderRequestDTO {

    @NotNull
    private PaymentType type;
    private String cardNumber;
    private String yearExpiry;
    private String monthExpiry;
    private String cardholderName;
    private Integer installment;

    public boolean validation() {
        if (type == PaymentType.CREDIT_CARD) {
            return !ObjectUtils.isEmpty(cardNumber) && !ObjectUtils.isEmpty(yearExpiry) && !ObjectUtils.isEmpty(monthExpiry) && !ObjectUtils.isEmpty(cardholderName) && !ObjectUtils.isEmpty(installment);
        }

        return true;
    }

    public PaymentOrder toPaymentOrder() {
        return PaymentOrder.builder()
                .type(type)
                .installment(installment)
                .build();
    }

}
