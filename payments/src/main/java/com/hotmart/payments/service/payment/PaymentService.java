package com.hotmart.payments.service.payment;

import com.hotmart.payments.dto.event.OrderEventDTO;
import com.hotmart.payments.dto.response.PaymentResponseDTO;
import com.hotmart.payments.enums.PaymentGateway;
import com.hotmart.payments.enums.PaymentStatus;
import com.hotmart.payments.enums.PaymentType;
import lombok.NonNull;

import java.time.LocalDateTime;
import java.util.List;


public interface PaymentService {

    void processPaymentSaga(@NonNull OrderEventDTO event);

    void rollbackPaymentSaga(@NonNull OrderEventDTO event);

    void requestRefund(@NonNull Long id);

    PaymentResponseDTO findById(@NonNull Long id);

    List<PaymentResponseDTO> findAll(@NonNull Long userId, String orderId, String transactionId, PaymentType type,
                                     PaymentStatus status, PaymentGateway gateway, LocalDateTime createdAt,
                                     LocalDateTime updatedAt);

}
