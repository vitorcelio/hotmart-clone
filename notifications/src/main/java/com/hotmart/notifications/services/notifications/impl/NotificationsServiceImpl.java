package com.hotmart.notifications.services.notifications.impl;

import com.hotmart.notifications.config.exception.ValidationException;
import com.hotmart.notifications.documents.Notifications;
import com.hotmart.notifications.dto.event.EventDTO;
import com.hotmart.notifications.dto.response.NotificationsResponseDTO;
import com.hotmart.notifications.enums.SentStatus;
import com.hotmart.notifications.repositories.NotificationsRepository;
import com.hotmart.notifications.services.notifications.NotificationsService;
import com.hotmart.notifications.services.security.SecurityService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationsServiceImpl implements NotificationsService {

    private final NotificationsRepository repository;
    private final SecurityService securityService;


    @Override
    public NotificationsResponseDTO save(@NonNull EventDTO event) {

        validationEventId(event.getEventId());

        Notifications notifications = Notifications.builder()
                .eventId(event.getEventId())
                .title(event.getTemplate().getTitle())
                .type(event.getType())
                .template(event.getTemplate())
                .senderId(securityService.getUserId())
                .recipientId(event.getUserId())
                .build();

        Notifications save = repository.save(notifications);

        return NotificationsResponseDTO.builder()
                .id(save.getId())
                .title(save.getTitle())
                .type(save.getType())
                .template(save.getTemplate())
                .senderId(save.getSenderId())
                .recipientId(save.getRecipientId())
                .createdAt(save.getCreatedAt())
                .sentAt(save.getSentAt())
                .viewedOrSent(save.isViewedOrSent())
                .build();
    }

    @Override
    public void viewedOrSent(@NonNull String id, @NonNull SentStatus status) {
        Notifications notifications = repository.findById(id).orElseThrow(() -> new ValidationException("Notificação não encontrada"));

        notifications.setViewedOrSent(true);
        notifications.setSentAt(LocalDateTime.now());
        notifications.setStatus(status);
        repository.save(notifications);
    }

    @Override
    public List<NotificationsResponseDTO> findAll() {
        List<Notifications> all = repository.findByRecipientIdAndStatus(securityService.getUserId(), SentStatus.SENT);
        return NotificationsResponseDTO.convert(all);
    }

    private void validationEventId(@NonNull String eventId) {
        Notifications notification = repository.findByEventId(eventId).orElse(null);

        if (notification != null) {
            throw new ValidationException("Evento já existe no banco de dados");
        }
    }

}
