package com.hotmart.notifications.services.email;

import com.hotmart.notifications.dto.event.EventDTO;

public interface EmailService {

    void execute(EventDTO event, String payload);

}
