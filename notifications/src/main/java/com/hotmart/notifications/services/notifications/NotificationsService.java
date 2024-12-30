package com.hotmart.notifications.services.notifications;

import com.hotmart.notifications.dto.event.EventDTO;
import com.hotmart.notifications.dto.response.NotificationsResponseDTO;
import com.hotmart.notifications.enums.SentStatus;
import lombok.NonNull;

import java.util.List;

public interface NotificationsService {

    NotificationsResponseDTO save(@NonNull EventDTO event);

    void viewedOrSent(@NonNull Long id, @NonNull SentStatus status);

    List<NotificationsResponseDTO> findAll();

}
