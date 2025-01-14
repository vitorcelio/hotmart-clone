package com.hotmart.orders.consumer;

import com.hotmart.orders.documents.OrderEvent;
import com.hotmart.orders.services.event.EventService;
import com.hotmart.orders.utils.JsonUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrdersConsumer {

    private final JsonUtil jsonUtil;
    private final EventService eventService;

    @KafkaListener(
            groupId = "${spring.kafka.consumer.group-id}",
            topics = "${spring.kafka.topic.notify-ending}"
    )
    public void consumerNotifyEndingEvent(String payload) {
        log.info("Recebendo evento [\n {} \n] do tópico notify-ending", payload);
        OrderEvent event = jsonUtil.toEvent(payload);
        eventService.notifyEnding(event);
    }

}
