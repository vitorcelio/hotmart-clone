package com.hotmart.orders.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hotmart.orders.documents.OrderEvent;
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

    private <T> T toObject(String json, Class<T> classType) {
        try {
            return objectMapper.readValue(json, classType);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public OrderEvent toEvent(String json) {
        return toObject(json, OrderEvent.class);
    }

}
