package com.hotmart.orders.repositories;

import com.hotmart.orders.documents.OrderEvent;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EventRepository extends MongoRepository<OrderEvent, String> {

    Optional<OrderEvent> findByTransactionId(String transactionId);

    Optional<OrderEvent> findTop1ByOrderIdOrderByCreatedAt(String orderId);

//    List<Event> findAllByOrderProductIdOrderByCreatedAtDesc(Long productId);

    List<OrderEvent> findAllByOrderProduct_IdOrderByCreatedAtDesc(Long productId);


}
