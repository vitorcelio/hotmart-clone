package com.hotmart.notifications.controller;

import com.hotmart.notifications.dto.response.MessageNotificationResponseDTO;
import com.hotmart.notifications.services.notifications.SseEmitterService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequestMapping("/api/v1/emitter")
@RequiredArgsConstructor
public class EmitterController {

    private final SseEmitterService service;

    @CrossOrigin(origins = { "http://localhost:5500", "http://127.0.0.1:5500" })
    @GetMapping("/subscribe/{userId}")
    public SseEmitter subscribe(@PathVariable("userId") Long userId) {
        return service.subscribeToNotifications(userId);
    }

    @PostMapping("/{userId}")
    public void sendNotification(@PathVariable("userId") Long userId) {
        service.sendNotification(userId, MessageNotificationResponseDTO.builder()
                .message("Testando")
                .nameSender("Vítor Célio")
                .imageSender("github.com/vitorcelio.png")
                .build());
    }

}
