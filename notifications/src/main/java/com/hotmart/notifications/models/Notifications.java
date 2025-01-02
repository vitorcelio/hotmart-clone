package com.hotmart.notifications.models;

import com.hotmart.notifications.enums.EventType;
import com.hotmart.notifications.enums.SentStatus;
import com.hotmart.notifications.enums.TemplateType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "notifications")
public class Notifications {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "event_id", nullable = false)
    private String eventId;

    @Column(nullable = false)
    private String title;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SentStatus status;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EventType type;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TemplateType template;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "sent_at")
    private LocalDateTime sentAt;

    @Column(name = "sender_id")
    private Long senderId;

    @Column(name = "recipient_id", nullable = false)
    private Long recipientId;

    @Column(nullable = false)
    private boolean viewedOrSent;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        this.viewedOrSent = false;
        this.status = SentStatus.PENDING;
    }

}
