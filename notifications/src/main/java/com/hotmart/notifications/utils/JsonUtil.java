package com.hotmart.notifications.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hotmart.notifications.dto.event.AccessDataEventDTO;
import com.hotmart.notifications.dto.event.EventDTO;
import com.hotmart.notifications.dto.event.RecoverPasswordEventDTO;
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

    public EventDTO toEvent(String json) {
        return toObject(json, EventDTO.class);
    }


    public RecoverPasswordEventDTO toRecoverPasswordEvent(String json) {
        return toObject(json, RecoverPasswordEventDTO.class);
    }

    public AccessDataEventDTO toAccessDataEvent(String json) {
        return toObject(json, AccessDataEventDTO.class);
    }

}
