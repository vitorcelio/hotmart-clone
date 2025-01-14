package com.hotmart.orders.services.order.impl;

import com.hotmart.orders.config.exception.ValidationException;
import com.hotmart.orders.documents.OrderEvent;
import com.hotmart.orders.documents.Order;
import com.hotmart.orders.dto.request.OrderRequestDTO;
import com.hotmart.orders.enums.SagaStatus;
import com.hotmart.orders.producer.KafkaProducer;
import com.hotmart.orders.repositories.OrderRepository;
import com.hotmart.orders.services.event.EventService;
import com.hotmart.orders.services.order.OrderService;
import com.hotmart.orders.utils.JsonUtil;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository repository;
    private final EventService eventService;
    private final KafkaProducer kafkaProducer;
    private final JsonUtil jsonUtil;

    @Value("${spring.kafka.topic.start-saga}")
    private String startSaga;

    @Override
    public Order save(@NonNull OrderRequestDTO request) {

        if (!request.getPayment().validation()) {
            throw new ValidationException("Os campos de pagamento são obrigatórios.");
        }

        Order order = Order.builder()
                .createdAt(LocalDateTime.now())
                .transactionId(String.format("%_%", Instant.now().toEpochMilli(), UUID.randomUUID()))
                .status(SagaStatus.PENDING)
                .product(request.getProduct().toProductOrder())
                .buyer(request.getBuyer().toBuyerOrder())
                .address(request.getAddress().toAddressOrder())
                .payment(request.getPayment().toPaymentOrder())
                .build();

        repository.save(order);
        OrderEvent event = eventService.createEvent(order);
        sendEventStartSaga(event);

        return order;
    }

    private void sendEventStartSaga(OrderEvent event) {
        String payload = jsonUtil.toJson(event);
        kafkaProducer.sendEvent(payload, startSaga);
    }

}
