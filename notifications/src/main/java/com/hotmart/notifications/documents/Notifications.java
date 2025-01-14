package com.hotmart.notifications.documents;

import com.hotmart.notifications.enums.EventType;
import com.hotmart.notifications.enums.SentStatus;
import com.hotmart.notifications.enums.TemplateType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Document(collection = "notifications")
public class Notifications {

    @Id
    private String id;
    private String eventId;
    private String title;
    private SentStatus status = SentStatus.PENDING;
    private EventType type;
    private TemplateType template;
    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime sentAt;
    private Long senderId;
    private Long recipientId;
    private boolean viewedOrSent = false;

}
