package com.hotmart.orders.repositories;

import com.hotmart.orders.documents.Event;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EventRepository extends MongoRepository<Event, String> {

    Optional<Event> findByTransactionId(String transactionId);

    Optional<Event> findTop1ByOrderIdOrderByCreatedAt(String orderId);

}
