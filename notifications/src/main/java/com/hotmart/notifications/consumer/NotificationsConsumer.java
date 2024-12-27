package com.hotmart.notifications.consumer;

import com.hotmart.notifications.dto.event.EventDTO;
import com.hotmart.notifications.services.email.EmailService;
import com.hotmart.notifications.utils.JsonUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;


@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationsConsumer {

    private final JsonUtil jsonUtil;
    private final EmailService emailService;

    @KafkaListener(groupId = "${spring.kafka.consumer.group-id}",
            topics = "${spring.kafka.topic.email}")
    public void consumerEmailEvent(String payload) {
        log.info("Recebendo evento {} do tópico email", payload);
        EventDTO event = jsonUtil.toEvent(payload);

        emailService.execute(event, payload);
    }

}
