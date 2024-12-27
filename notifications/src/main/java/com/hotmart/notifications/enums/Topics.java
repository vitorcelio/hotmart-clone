package com.hotmart.notifications.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Topics {

    NOTIFICATION("notification"),
    ACCOUNT("account"),
    AUTHENTICATION("authentication"),;

    private final String name;

}
