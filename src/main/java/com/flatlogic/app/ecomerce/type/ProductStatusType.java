package com.flatlogic.app.ecomerce.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Objects;

@Getter
@RequiredArgsConstructor
public enum ProductStatusType {

    IN_STOCK("in stock"), OUT_OF_STOCK("out of stock");

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
