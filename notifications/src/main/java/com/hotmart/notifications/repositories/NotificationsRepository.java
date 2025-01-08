package com.hotmart.notifications.repositories;

import com.hotmart.notifications.documents.Notifications;
import com.hotmart.notifications.enums.SentStatus;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NotificationsRepository extends MongoRepository<Notifications, String> {

    List<Notifications> findByRecipientIdAndStatus(Long recipientId, SentStatus status);

    Optional<Notifications> findByEventId(String eventId);

}
