package com.bistrocheese.foodservice.dto.response;

import com.bistrocheese.foodservice.model.Category;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.*;


import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
public class FoodResponse {
    @JsonProperty("id")
    private UUID id;
    @JsonProperty("name")
    private String name;
    @JsonProperty("category")
    private Category category;
    @JsonProperty("description")
    private String description;
    @JsonProperty("foodImage")
    private String foodImage;
    @JsonProperty("price")
    private BigDecimal price;
    @JsonProperty("status")
    private Integer status;
}
