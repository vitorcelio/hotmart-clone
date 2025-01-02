package com.hotmart.notifications.respositories;

import com.hotmart.notifications.enums.SentStatus;
import com.hotmart.notifications.models.Notifications;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NotificationsRepository extends JpaRepository<Notifications, Long> {

    List<Notifications> findByRecipientIdAndStatus(Long recipientId, SentStatus status);

    Optional<Notifications> findByEventId(String eventId);

}
