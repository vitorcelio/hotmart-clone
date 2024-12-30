package com.hotmart.notifications.services.notifications;

import com.hotmart.notifications.dto.response.MessageNotificationResponseDTO;
import lombok.NonNull;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public interface SseEmitterService {

    SseEmitter subscribeToNotifications(@NonNull Long userId);

    void sendNotification(@NonNull Long userId, @NonNull MessageNotificationResponseDTO message);

    void removeEmitter(@NonNull Long userId, @NonNull SseEmitter emitter);

}
