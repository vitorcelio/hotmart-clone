package com.hotmart.orders.services.event.impl;

import com.hotmart.orders.config.exception.ValidationException;
import com.hotmart.orders.documents.OrderEvent;
import com.hotmart.orders.documents.Order;
import com.hotmart.orders.repositories.EventRepository;
import com.hotmart.orders.services.event.EventService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private final EventRepository repository;

    @Override
    public void notifyEnding(OrderEvent event) {
        event.setOrderId(event.getOrderId());
        event.setCreatedAt(LocalDateTime.now());
        repository.save(event);
        log.info("Pedido {} finalizado: TransactionId {}", event.getOrderId(), event.getTransactionId());
    }

    @Override
    public OrderEvent createEvent(Order order) {
        OrderEvent event = OrderEvent.builder()
                .orderId(order.getId())
                .order(order)
                .transactionId(order.getTransactionId())
                .createdAt(LocalDateTime.now())
                .build();

        return repository.save(event);
    }

    @Override
    public OrderEvent findByOrderId(@NonNull String orderId) {
        return repository.findTop1ByOrderIdOrderByCreatedAt(orderId).orElseThrow(() -> new ValidationException("Pedido não encontrado"));
    }

    @Override
    public OrderEvent findByTransactionId(@NonNull String transactionId) {
        return repository.findByTransactionId(transactionId).orElseThrow(() -> new ValidationException("Transação do evento não encontrada"));
    }

    @Override
    public List<OrderEvent> findAllByProductId(@NonNull Long productId) {
        return repository.findAllByOrderProduct_IdOrderByCreatedAtDesc(productId);
    }
}
