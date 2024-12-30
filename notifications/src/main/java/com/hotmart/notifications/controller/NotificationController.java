package com.hotmart.notifications.controller;

import com.hotmart.notifications.dto.response.NotificationsResponseDTO;
import com.hotmart.notifications.services.notifications.NotificationsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/notifications")
public class NotificationController {

    private final NotificationsService service;

    @GetMapping("/all")
    public ResponseEntity<List<NotificationsResponseDTO>> findAll() {
        return new ResponseEntity<>(service.findAll(), HttpStatus.OK);
    }

}
