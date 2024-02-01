package com.flatlogic.app.ecomerce.type;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Objects;

@Getter
@RequiredArgsConstructor
public enum ProductStatusType {

    @JsonProperty("in stock") IN_STOCK("in stock"),
    @JsonProperty("out of stock") OUT_OF_STOCK("out of stock");

    private final String status;

    public static ProductStatusType valueOfStatus(String status) {
        for (ProductStatusType entry : values()) {
            if (Objects.equals(entry.status, status)) {
                return entry;
            }
        }
        return null;
    }

}
