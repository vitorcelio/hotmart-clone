package com.hotmart.notifications.documents;

import com.hotmart.notifications.enums.EventType;
import com.hotmart.notifications.enums.SentStatus;
import com.hotmart.notifications.enums.TemplateType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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

    @NotBlank
    private String eventId;

    @NotBlank
    private String title;

    @NotNull
    private SentStatus status = SentStatus.PENDING;

    @NotNull
    private EventType type;

    @NotNull
    private TemplateType template;

    @NotNull
    private LocalDateTime createdAt = LocalDateTime.now();

    private LocalDateTime sentAt;

    @NotNull
    private Long senderId;

    @NotNull
    private Long recipientId;

    @NotNull
    private boolean viewedOrSent = false;

}
