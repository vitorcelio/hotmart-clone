package com.hotmart.notifications.services.notifications.impl;

import com.hotmart.notifications.config.exception.ValidationException;
import com.hotmart.notifications.dto.response.MessageNotificationResponseDTO;
import com.hotmart.notifications.services.notifications.SseEmitterService;
import com.hotmart.notifications.services.security.SecurityService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Slf4j
@Service
@RequiredArgsConstructor
public class SseEmitterServiceImpl implements SseEmitterService {

    private final Map<Long, List<SseEmitter>> userSseEmitters = new ConcurrentHashMap<>();
    private final SecurityService securityService;

    @Override
    public SseEmitter subscribeToNotifications(@NonNull Long userId) {

        validationCreationSubscribe(userId);

        SseEmitter sseEmitter = new SseEmitter();
        initializeSseEmitters(userId, sseEmitter);

        sseEmitter.onCompletion(() -> removeEmitter(userId, sseEmitter));
        sseEmitter.onTimeout(() -> removeEmitter(userId, sseEmitter));
        sseEmitter.onError((e) -> removeEmitter(userId, sseEmitter));
        return sseEmitter;
    }

    @Override
    public void sendNotification(@NonNull Long userId, @NonNull MessageNotificationResponseDTO message) {
        List<SseEmitter> sseEmitters = userSseEmitters.get(userId);
        if (!ObjectUtils.isEmpty(sseEmitters)) {
            for (SseEmitter emitter : sseEmitters) {
                try {
                    emitter.send(SseEmitter.event().data(message).build());
                    log.info("Notificação enviada para usuário [{}]", userId);
                } catch (Exception e) {
                    removeEmitter(userId, emitter);
                }
            }
        } else {
            SseEmitter sseEmitter = new SseEmitter();
            initializeSseEmitters(userId, sseEmitter);
        }
    }

    @Override
    public void removeEmitter(@NonNull Long userId, @NonNull SseEmitter emitter) {
        List<SseEmitter> sseEmitters = userSseEmitters.get(userId);
        if (!ObjectUtils.isEmpty(sseEmitters)) {
            sseEmitters.remove(emitter);
            if (sseEmitters.isEmpty()) {
                userSseEmitters.remove(userId);
                log.info("Emissor de notificação removido para usuário [{}]", userId);
            }
        }
    }

    private void initializeSseEmitters(@NonNull Long userId, @NonNull SseEmitter sseEmitter) {
        userSseEmitters.computeIfAbsent(userId, id -> new CopyOnWriteArrayList<>()).add(sseEmitter);
        log.info("Usuário [{}] inscrito para receber notificações", userId);
    }

    private void validationCreationSubscribe(@NonNull Long userId) {

        if (securityService.getUserId() == null) {
            throw new ValidationException("Usuário não autenticado!");
        }

        if (!securityService.getUserId().equals(userId)) {
            throw new ValidationException("Usuário sem permissão!");
        }

    }
}
