package com.hotmart.orders.producer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class KafkaProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public void sendEvent(String payload, String topic) {
        try {
            log.info("Sending event: {} to topic {}", payload, topic);
            kafkaTemplate.send(topic, payload);
            log.info("Event sent to topic {}", topic);
        } catch (Exception e) {
            log.error("Error while sending message to topic {}", topic, e);
        }
    }

}
