package com.hotmart.products.consumer;

import com.hotmart.products.dto.event.OrderEventDTO;
import com.hotmart.products.services.buyer.BuyerService;
import com.hotmart.products.services.product.ProductService;
import com.hotmart.products.utils.JsonUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProductConsumer {

    private final ProductService productService;
    private final BuyerService buyerService;
    private final JsonUtil jsonUtil;

    @KafkaListener(
            groupId = "${spring.kafka.consumer.group-id}",
            topics = "${spring.kafka.topic.product-start}"
    )
    public void consumerSuccessEvent(String payload) {
        log.info("Recebendo evento [\n{}\n] do tópico product-success", payload);
        OrderEventDTO event = jsonUtil.toEvent(payload);
        productService.validateProductSaga(event);
    }

    @KafkaListener(
            groupId = "${spring.kafka.consumer.group-id}",
            topics = "${spring.kafka.topic.product-fail}"
    )
    public void consumerFailEvent(String payload) {
        log.info("Recebendo evento [\n{}\n] do tópico product-fail", payload);
        OrderEventDTO event = jsonUtil.toEvent(payload);
        productService.rollbackProductSaga(event);
    }

    @KafkaListener(
            groupId = "${spring.kafka.consumer.group-id}",
            topics = "${spring.kafka.topic.buyer-success}"
    )
    public void consumerSuccessBuyerEvent(String payload) {
        log.info("Recebendo evento [\n{}\n] do tópico buyer-success", payload);
        OrderEventDTO event = jsonUtil.toEvent(payload);
        buyerService.createBuyerSaga(event);
    }

    @KafkaListener(
            groupId = "${spring.kafka.consumer.group-id}",
            topics = "${spring.kafka.topic.buyer-fail}"
    )
    public void consumerFailBuyerEvent(String payload) {
        log.info("Recebendo evento [\n{}\n] do tópico buyer-fail", payload);
        OrderEventDTO event = jsonUtil.toEvent(payload);
        buyerService.rollbackBuyerSaga(event);
    }

}
