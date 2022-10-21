package com.flatlogic.app.generator.controller.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public class OrderRequest {

    @JsonProperty("order_date")
    private LocalDateTime orderDate;

    private Integer amount;

    private String status;

    private String importHash;

    @JsonProperty("product")
    private UUID productId;

    @JsonProperty("user")
    private UUID userId;

}
