package com.loiko.alex.contractservice.service.client;

import com.loiko.alex.contractservice.model.Customer;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("customer-service")
public interface CustomerFeignClient {

    @GetMapping(value = "/v1/customer/{customerId}", consumes = "application/json")
    Customer getCustomer(@PathVariable("customerId") String customerId);
}