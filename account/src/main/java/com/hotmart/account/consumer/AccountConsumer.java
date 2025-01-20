package com.hotmart.account.consumer;

import com.hotmart.account.dto.event.OrderEventDTO;
import com.hotmart.account.services.user.UserService;
import com.hotmart.account.utils.JsonUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class AccountConsumer {

    private final UserService userService;
    private final JsonUtil jsonUtil;

    @KafkaListener(
            groupId = "${spring.kafka.consumer.group-id}",
            topics = "${spring.kafka.topic.user-success}"
    )
    public void consumerSuccessEvent(String payload) {
        log.info("Recebendo evento [\n{}\n] do tópico user-success", payload);
        OrderEventDTO event = jsonUtil.toEvent(payload);
        userService.createUserSaga(event);
    }

}
