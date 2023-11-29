package com.bistrocheese.userservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient("orderservice")
public interface OrderFeignClient {
    @RequestMapping(value = "order-service/api/orders", method = RequestMethod.GET)
    public ResponseEntity<String> placeOrder(@RequestBody String staffId);
}
