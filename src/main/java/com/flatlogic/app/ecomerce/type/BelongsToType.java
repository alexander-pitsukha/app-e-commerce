package com.flatlogic.app.ecomerce.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum BelongsToType {

    PRODUCTS("products"), USERS("users");

    private final String type;

}
