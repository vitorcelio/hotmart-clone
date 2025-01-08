package com.hotmart.notifications.utils;

import java.time.Instant;
import java.util.UUID;

public class NotificationsUtils {

    private static final String NOTIFICATIONS_ID_PATTERN = "%s_s%";

    public static String generatedId() {
        return String.format(NOTIFICATIONS_ID_PATTERN, Instant.now().toEpochMilli(), UUID.randomUUID());
    }

}
