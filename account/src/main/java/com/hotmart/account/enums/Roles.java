package com.hotmart.account.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Roles {

    SELLER(1),
    BUYER(2),
    ADMIN(3);

    private final int id;

}
