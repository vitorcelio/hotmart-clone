package com.hotmart.notifications.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageNotificationResponseDTO {

    private String message;
    private String nameSender;
    private String imageSender;


}
