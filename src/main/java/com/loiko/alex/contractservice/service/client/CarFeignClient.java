package com.loiko.alex.contractservice.service.client;

import com.loiko.alex.contractservice.model.Car;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("car-service")
public interface CarFeignClient {

    @GetMapping(value = "/v1/car/{carId}", consumes = "application/json")
    Car getCar(@PathVariable("carId") String carId);
}