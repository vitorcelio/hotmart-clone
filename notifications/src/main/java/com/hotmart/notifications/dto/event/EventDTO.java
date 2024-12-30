package com.hotmart.notifications.dto.event;

import com.hotmart.notifications.enums.EventType;
import com.hotmart.notifications.enums.TemplateType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventDTO {

    private String eventId;
    private EventType type;
    private TemplateType template;
    private String email;
    private Long userId;
    private String name;

}
