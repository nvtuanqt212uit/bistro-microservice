package com.bistrocheese.userservice.client;

import com.bistrocheese.userservice.dto.response.FoodResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.UUID;

import static com.bistrocheese.userservice.constant.ServiceConstant.FOOD_SERVICE;

@FeignClient(FOOD_SERVICE)
public interface FoodFeignClient {
    @RequestMapping(value = "food-service/api/foods/{foodId}", method = RequestMethod.GET)
    ResponseEntity<FoodResponse> getDetailFood(@PathVariable("foodId") UUID foodId);
}
