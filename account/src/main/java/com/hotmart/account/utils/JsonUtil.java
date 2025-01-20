package com.hotmart.account.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hotmart.account.dto.event.OrderEventDTO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class JsonUtil {

    private final ObjectMapper objectMapper;

    public String toJson(Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public OrderEventDTO toEvent(String json) {
        try {
            return objectMapper.readValue(json, OrderEventDTO.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
