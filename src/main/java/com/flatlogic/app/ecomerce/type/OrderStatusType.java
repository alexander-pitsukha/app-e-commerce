package com.flatlogic.app.ecomerce.type;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Objects;

@Getter
@RequiredArgsConstructor
public enum OrderStatusType {

    @JsonProperty("in cart") IN_CART("in cart"),
    @JsonProperty("bought") BOUGHT("bought");

    private final String status;

    public static OrderStatusType valueOfStatus(String status) {
        for (OrderStatusType entry : values()) {
            if (Objects.equals(entry.status, status)) {
                return entry;
            }
        }
        return null;
    }

}
