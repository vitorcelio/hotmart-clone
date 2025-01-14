package com.hotmart.orders.services.event;

import com.hotmart.orders.documents.OrderEvent;
import com.hotmart.orders.documents.Order;
import lombok.NonNull;

import java.util.List;

public interface EventService {

    void notifyEnding(OrderEvent event);

    OrderEvent createEvent(Order order);

    OrderEvent findByOrderId(@NonNull String orderId);

    OrderEvent findByTransactionId(@NonNull String transactionId);

    List<OrderEvent> findAllByProductId(@NonNull Long productId);

}
