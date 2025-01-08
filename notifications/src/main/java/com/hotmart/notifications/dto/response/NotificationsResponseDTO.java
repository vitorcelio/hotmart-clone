package com.hotmart.notifications.dto.response;

import com.hotmart.notifications.enums.EventType;
import com.hotmart.notifications.enums.SentStatus;
import com.hotmart.notifications.enums.TemplateType;
import com.hotmart.notifications.documents.Notifications;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationsResponseDTO {

    private String id;
    private String title;
    private SentStatus status;
    private EventType type;
    private TemplateType template;
    private LocalDateTime createdAt;
    private LocalDateTime sentAt;
    private Long senderId;
    private Long recipientId;
    private boolean viewedOrSent;

    public NotificationsResponseDTO(Notifications notification) {
        this.id = notification.getId();
        this.title = notification.getTitle();
        this.status = notification.getStatus();
        this.type = notification.getType();
        this.template = notification.getTemplate();
        this.createdAt = notification.getCreatedAt();
        this.sentAt = notification.getSentAt();
        this.senderId = notification.getSenderId();
        this.recipientId = notification.getRecipientId();
        this.viewedOrSent = notification.isViewedOrSent();
    }

    public static List<NotificationsResponseDTO> convert(List<Notifications> notifications) {
        return notifications.stream().map(NotificationsResponseDTO::new).collect(Collectors.toList());
    }

}
