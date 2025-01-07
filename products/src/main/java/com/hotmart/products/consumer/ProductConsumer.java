package com.hotmart.products.consumer;

import com.hotmart.products.services.buyer.BuyerService;
import com.hotmart.products.utils.JsonUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProductConsumer {

    private final BuyerService buyerService;
    private final JsonUtil jsonUtil;

    @KafkaListener(
            groupId = "${spring.kafka.consumer.group-id}",
            topics = "${spring.kafka.topic.product-success}"
    )
    public void consumerSuccessEvent(String payload) {
        log.info("Recebendo evento [\n{}\n] do tópico product-success", payload);
    }

    @KafkaListener(
            groupId = "${spring.kafka.consumer.group-id}",
            topics = "${spring.kafka.topic.product-fail}"
    )
    public void consumerFailEvent(String payload) {
        log.info("Recebendo evento [\n{}\n] do tópico product-fail", payload);
    }

}
