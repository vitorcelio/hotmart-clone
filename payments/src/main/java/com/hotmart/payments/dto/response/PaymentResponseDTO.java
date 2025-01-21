package com.hotmart.payments.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hotmart.payments.enums.PaymentGateway;
import com.hotmart.payments.enums.PaymentStatus;
import com.hotmart.payments.enums.PaymentType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentResponseDTO {

    private Long id;
    private String integrationId;
    private String orderId;
    private String transactionId;
    private Long userId;
    private Integer totalItems;
    private BigDecimal amount;
    private PaymentType type;
    private PaymentStatus status;
    private PaymentGateway gateway;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
