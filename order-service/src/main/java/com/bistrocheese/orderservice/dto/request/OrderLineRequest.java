package com.bistrocheese.orderservice.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
public class OrderLineRequest {
    @JsonProperty("food_id")
    private UUID foodId;

    @JsonProperty("quantity")
    private Integer quantity;

    @JsonProperty("price")
    private BigDecimal price;
}
