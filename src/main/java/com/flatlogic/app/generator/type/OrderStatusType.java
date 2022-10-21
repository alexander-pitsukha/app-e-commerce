package com.flatlogic.app.generator.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum OrderStatusType {

    IN_CART("in cart"), BOUGHT("bought");

    private final String status;

    public static OrderStatusType valueOfStatus(String status) {
        for (OrderStatusType entry : values()) {
            if (entry.status.equals(status)) {
                return entry;
            }
        }
        return null;
    }

}
