package com.flatlogic.app.generator.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ProductStatusType {

    IN_STOCK("in stock"), OUT_OF_STOCK("out of stock");

    private final String status;

    public static ProductStatusType valueOfStatus(String status) {
        for (ProductStatusType entry : values()) {
            if (entry.status.equals(status)) {
                return entry;
            }
        }
        return null;
    }

}
