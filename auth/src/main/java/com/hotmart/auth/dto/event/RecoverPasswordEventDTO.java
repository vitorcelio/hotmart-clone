package com.hotmart.auth.dto.event;

import com.hotmart.auth.enums.EventType;
import com.hotmart.auth.enums.TemplateType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class RecoverPasswordEventDTO {

    // Padrão dos eventos
    private String eventId;
    private EventType type;
    private TemplateType template;
    private Long userId;
    private String email;
    private String name;

    private String pswdrst;
    private String uuid;

}
