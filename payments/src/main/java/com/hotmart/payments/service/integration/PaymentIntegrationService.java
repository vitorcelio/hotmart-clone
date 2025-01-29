package com.hotmart.payments.service.integration;

import com.asaas.docs.dto.response.DeleteResponseDTO;
import com.asaas.docs.dto.response.PaymentResponseDTO;
import com.hotmart.payments.dto.event.OrderEventDTO;
import com.hotmart.payments.models.Payment;
import lombok.NonNull;

public interface PaymentIntegrationService {

    PaymentResponseDTO createPayment(@NonNull Payment payment, @NonNull String customerId, @NonNull OrderEventDTO event);

    PaymentResponseDTO getPayment(String integrationId);

    DeleteResponseDTO deletePayment(String integrationId);

    PaymentResponseDTO refundPayment(String integrationId);

}
